import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class PhoneCountReducer extends Reducer<Text, TrafficBean, Text, TrafficBean> {

    @Override
    public void reduce(Text key, Iterable<TrafficBean> values, Context context) throws IOException,InterruptedException{
        long sum_upTraffic= 0;
        long sum_downTraffic = 0;
        for(TrafficBean value:values){
            sum_upTraffic += value.getUpTraffic();
            sum_downTraffic += value.getDownTraffic();
        }

        TrafficBean resultTraffic = new TrafficBean(sum_upTraffic, sum_downTraffic);
        context.write(key, resultTraffic);

    }
}
