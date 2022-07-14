package test;

import java.io.Serializable;

public class SerialTest implements Serializable {

    private final static long serialVersionUID = 123456789L;

    private String name;

    private String sex;


    @Override
    public String toString() {
        return "SerialTest{" +
                "name='" + name + '\'' +
                '}';
    }
}
