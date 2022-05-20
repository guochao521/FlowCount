package flowcount;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;

public class FlowReducer extends Reducer<Text, FlowBean,Text, FlowBean> {
    int sum_low = 0;
    int sum_up = 0;
    FlowBean v = new FlowBean();
    @Override
    protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException {
        for (FlowBean b:values){
            sum_up+=b.getSum_up();
            sum_low+=b.getSum_low();
        }
        v.set(sum_low,sum_up,sum_low+sum_up);
        context.write(key,v);
        sum_up=0;
        sum_low=0;
    }
}
