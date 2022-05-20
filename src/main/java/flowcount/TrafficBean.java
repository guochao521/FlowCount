package flowcount;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TrafficBean extends LongWritable implements Writable {

    private long upTraffic;
    private long downTraffic;
    private long sumTraffic;

    public TrafficBean(){

    }

    public TrafficBean(long upTraffic, long downTraffic){
        this.upTraffic = upTraffic;
        this.downTraffic = downTraffic;
        this.sumTraffic = upTraffic + downTraffic;
    }

    public long getUpTraffic(){
        return upTraffic;
    }
    public void setUpTraffic(long upTraffic){
        this.upTraffic = upTraffic;
    }

    public long getDownTraffic() {
        return downTraffic;
    }
    public void setDownTraffic(long downTraffic){
        this.downTraffic = downTraffic;
    }

    public long getSumTraffic() {
        return sumTraffic;
    }
    public void setSumTraffic(long sumTraffic) {
        this.sumTraffic = sumTraffic;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeLong(upTraffic);
        dataOutput.writeLong(downTraffic);
        dataOutput.writeLong(sumTraffic);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        upTraffic = dataInput.readLong();
        downTraffic = dataInput.readLong();
        sumTraffic = dataInput.readLong();
    }

    @Override
    public String toString(){
        return upTraffic + "\t" + downTraffic + "\t" + sumTraffic;
    }
}
