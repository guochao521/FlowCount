import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class PhoneCountMapper extends Mapper<LongWritable, Text, Text, TrafficBean> {

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException,InterruptedException{
        // 读取一行数据
        String line = value.toString();
        // 切分字段
        String[] columns = line.split("\t");
        // 取出信息
        String phone = columns[1]; // 电话号码
        long upTraffic = Long.parseLong(columns[8]); // 上行流量
        long downTraffic = Long.parseLong(columns[9]); // 下行流量
        System.out.println(line);

        context.write(new Text(phone), new TrafficBean(upTraffic, downTraffic));
    }
}
