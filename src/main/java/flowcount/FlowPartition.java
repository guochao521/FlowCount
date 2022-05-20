package flowcount;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class FlowPartition extends Partitioner<Text, FlowBean> {
    @Override
    public int getPartition(Text text, FlowBean bean, int i) {
        String prePhone =text.toString().substring(0,3);//substring左闭右开
        int partition = 4;//五个分区,从0开始算
        if("136".equals(prePhone)){
            partition = 0;
        }else if ("137".equals(prePhone)){
            partition = 1;
        }else if ("138".equals(prePhone)){
            partition = 2;
        }else if ("139".equals(prePhone)){
            partition = 3;
        }else {
            partition = 4;
        }
        return partition;
    }
}