import org.apache.hadoop.io.WritableComparable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class FlowBean implements WritableComparable<FlowBean> {
    private int sum_low;
    private int sum_up;
    private int sum_bean;

    @Override
    public int compareTo(FlowBean o) {
        return this.sum_bean - o.sum_bean;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(sum_low);
        dataOutput.writeInt(sum_up);
        dataOutput.writeInt(sum_bean);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        sum_low = dataInput.readInt();
        sum_up = dataInput.readInt();
        sum_bean = dataInput.readInt();
    }

    public void set(int sum_low, int sum_up, int sum_bean) {
        this.sum_low = sum_low;
        this.sum_up = sum_up;
        this.sum_bean = sum_bean;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlowBean bean = (FlowBean) o;
        return sum_low == bean.sum_low &&
                sum_up == bean.sum_up &&
                sum_bean == bean.sum_bean;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sum_low, sum_up, sum_bean);
    }

    @Override
    public String toString() {
        return sum_low + "\t" + sum_up + "\t" + sum_bean;
    }

    public int getSum_low() {
        return sum_low;
    }

    public void setSum_low(int sum_low) {
        this.sum_low = sum_low;
    }

    public int getSum_up() {
        return sum_up;
    }

    public void setSum_up(int sum_up) {
        this.sum_up = sum_up;
    }

    public int getSum_bean() {
        return sum_bean;
    }

    public void setSum_bean(int sum_bean) {
        this.sum_bean = sum_bean;
    }
}