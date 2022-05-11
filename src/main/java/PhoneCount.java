import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
//import org.apache.hadoop.mapred.FileInputFormat;
//import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Job;

import java.io.*;
import java.util.List;

public class PhoneCount {

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Configuration conf = new Configuration();
        conf.set("mapreduce.framework.name", "local");
        conf.set("fs.default","hdfs://10.10.10.175:9000/");
        Job job = Job.getInstance(conf,"PhoneCount");

        // 指定本程序的jar包所在的本地路径
        job.setJarByClass(PhoneCount.class);
//        job.setJobName("PhoneCount app");

        // 设置map函数类
        job.setMapperClass(PhoneCountMapper.class);
        // 设置reduce函数类
        job.setReducerClass(PhoneCountReducer.class);

        // 指定最终输出的数据的kv类型
        job.setOutputKeyClass(Text.class);
        job.setOutputKeyClass(TrafficBean.class);

//        // 指定job的输入原始文件所在目录
        FileInputFormat.addInputPath(job, new Path("D:/DataSet/HTTP_20130313143750.dat"));
        FileOutputFormat.setOutputPath(job, new Path("output"));

//        FileInputFormat.addInputPath(job, new Path(args[0]));
//        FileOutputFormat.setOutputPath(job, new Path(args[1]));

//        Path path = new Path(args[1]);
        Path path = new Path("output");
        FileSystem fileSystem = path.getFileSystem(conf);
        if(fileSystem.exists(path)){
            fileSystem.delete(path, true);
        }

        // 将job中配置的相关参数，以及job所用的 java类所在的jar包，提交给yarn去运行
        System.exit(job.waitForCompletion(true) ? 0:1);
    }
}
