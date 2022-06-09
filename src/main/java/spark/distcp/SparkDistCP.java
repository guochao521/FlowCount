package spark.distcp;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SparkDistCP {

    private static final SparkConf sparkConf;
    private static final SparkContext sparkContext;
    private static final JavaSparkContext javaSparkContext;
    private static Configuration configuration;

    static {
        sparkConf = new SparkConf();
        sparkConf.set("spark.master", "local[*]");
        sparkConf.set("spark.app.name", "SparkDistCP");
        sparkContext = SparkContext.getOrCreate(sparkConf);
        javaSparkContext = new JavaSparkContext(sparkContext);
        configuration = sparkContext.hadoopConfiguration();
    }

    public static void main(String[] args) throws IOException {
        String sourceRootPath = ""; // args[0]
        String targetRootPath = ""; // args[1]

        int maxCountCurrency = Integer.parseInt(args[2]);  // 设置并行度
        boolean ignoreFailure = Boolean.parseBoolean(args[3]);

        JavaRDD<String> sourceFileListRDD = getSourceFileLists(sourceRootPath, targetRootPath, maxCountCurrency);

        sourceFileListRDD.foreachPartition(sourceFileIterator -> {
            FileSystem sourceFileSystem = new Path(sourceRootPath).getFileSystem(configuration);
            FileSystem targetFileSystem = new Path(targetRootPath).getFileSystem(configuration);
            // 拷贝文件
            while (sourceFileIterator.hasNext()){
                String sourceFilePath = sourceFileIterator.next();
                Path sourceFileRelativePath = new Path(new Path(sourceRootPath).toUri().relativize(new Path(sourceFilePath).toUri()));
                Path targetPath = new Path(targetRootPath, sourceFileRelativePath);
                try(InputStream sourceInputStream = sourceFileSystem.open(new Path(sourceFilePath));
                    FSDataOutputStream fsDataOutputStream = targetFileSystem.create(targetPath, true)) {
                    IOUtils.copy(sourceInputStream, fsDataOutputStream);
                } catch (Throwable th){
                    if(!ignoreFailure){
                        throw th;
                    }
                }
            }
        });
    }

    private static JavaRDD<String> getSourceFileLists(String sourceRootPathStr, String targetRootPathStr, int maxConcurrency) throws IOException {
        Path sourceRootPath = new Path(sourceRootPathStr);
        Path targetRootPath = new Path(targetRootPathStr);
        FileSystem sourceFileSystem = sourceRootPath.getFileSystem(configuration);
        FileSystem targetFileSystem = targetRootPath.getFileSystem(configuration);
        RemoteIterator<LocatedFileStatus> iterator = sourceFileSystem.listFiles(sourceRootPath, true);
        Set<Path> distinctDirPaths = new HashSet<>();
        List<String> fileList = new ArrayList<>();
        while(iterator.hasNext()) {
            LocatedFileStatus locatedFileStatus = iterator.next();
            Path filePath = locatedFileStatus.getPath();
            distinctDirPaths.add(filePath.getParent());
            fileList.add(filePath.toString());
        }
        distinctDirPaths.remove(sourceRootPath);
        for(Path distinctDirPath : distinctDirPaths) {
            String sourceChildrenDirRelativePathStr = sourceRootPath.toUri().relativize(distinctDirPath.toUri()).toString();
            targetFileSystem.mkdirs(new Path(targetRootPath, sourceChildrenDirRelativePathStr), new FsPermission(FsAction.ALL, FsAction.READ, FsAction.READ));
        }
        return javaSparkContext.parallelize(fileList, maxConcurrency);
    }
}
