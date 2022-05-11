import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.fs.Path;

import java.io.File;


public class FlowDriver {
    public static void main(String[] args) throws Exception{
        Path inputPath = new Path(args[0]);
        Path outputPath = new Path(args[1]);
        File outFile = new File(args[1]);
//        File file = new File("D:\\FlowSum\\output");
        if (outFile.exists()){
            delFile(outFile);
            driver(inputPath, outputPath);
        }else {
            driver(inputPath, outputPath);
        }
    }
    public static void delFile(File file) {
        File[] files = file.listFiles();
        if (files != null && files.length != 0) {
            for (int i = 0;i<files.length;i++) {
                delFile(files[i]);
            }
        }
        file.delete();
    }
    public  static void driver(Path inputPath, Path outputPath) throws Exception{
        Configuration conf = new Configuration();
//        conf.set("fs.default","hdfs://10.10.10.175:9000/");
        conf.set("mapreduce.framework.name", "local");
        Job job = Job.getInstance(conf, "FlowCount");

        // 指定本程序的jar包所在的本地路径
        job.setMapperClass(FlowMap.class);
        job.setReducerClass(FlowReducer.class);
        job.setJarByClass(FlowDriver.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FlowBean.class);

        job.setOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FlowBean.class);

        job.setNumReduceTasks(5);
        job.setPartitionerClass(FlowPartition.class);

//        FileInputFormat.setInputPaths(job, "D:/DataSet/HTTP_20130313143750.dat");
//        FileOutputFormat.setOutputPath(job, new Path("output"));

        FileInputFormat.addInputPath(job, inputPath);
        FileOutputFormat.setOutputPath(job, outputPath);

        boolean b = job.waitForCompletion(true);
        System.exit(b ? 0 : 1);
    }
}
