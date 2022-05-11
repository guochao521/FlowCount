import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

/**
 * 1363157986072 	18320173382	84-25-DB-4F-10-1A:CMCC-EASY	120.196.100.99	input.shouji.sogou.com	搜索引擎	21	18	9531	2412	200
 * 求每个手机号的上行流量之和、下行流量之和、上下行流量之和
 */
public class FlowMap extends Mapper<LongWritable, Text, Text, FlowBean> {
    FlowBean v = new FlowBean();
    Text k = new Text();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String [] datas = value.toString().split("\t");
//        System.out.println(datas);
        k.set(datas[1]);
        //这里先暂时存储单个的上下行流量
        v.set(Integer.parseInt(datas[datas.length-3]),Integer.parseInt(datas[datas.length-2]),0);
        context.write(k,v);
    }
}