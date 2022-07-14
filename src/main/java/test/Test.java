package test;

import javafx.util.Pair;

import java.math.BigDecimal;
import java.util.*;
import java.time.LocalDate;
import java.util.stream.Collectors;
//import java.sql.Date;



public class Test {

    private static Object Date;

    public static void main(String[] args) {
        
        float a = 1.0F - 0.9F;
        float b = 0.9F - 0.8F;
//        System.out.println(a);
//        System.out.println(b);


        System.out.println(LocalDate.of(2011, 1, 1).lengthOfYear());


        BigDecimal a1 = new BigDecimal("1.0");
        BigDecimal b1 = new BigDecimal("0.9");
        BigDecimal c1 = new BigDecimal("0.8");
        BigDecimal x = a1.subtract(b1);
        BigDecimal y = b1.subtract(c1);
        System.out.println(x);
        System.out.println(y);
        System.out.println(x.compareTo(y));
        System.out.println(x.equals(y));

        System.out.println("0" + Integer.toBinaryString(Float.floatToIntBits(0.8f)));


        System.out.println(System.currentTimeMillis());
        System.out.println(new Date().getTime()); // 调用了 System.currentTimeMillis()



        // 时间类
        int[] dayArray = new int[365];
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, 1, 26);
        calendar.add(Calendar.DATE, 365);

        System.out.println(Calendar.JANUARY);

        System.out.println(calendar.getTime());

//        ((RDC，1)()) RDC->null，RDC->null,KKb->null
//
//                123,rdc;123,rdc;789,kkb
        String[] departments = new String[]{"RDC1", "RDC", "KKB"};
        // 抛出IllegalStateException异常
        Map<Integer, String> map11 = Arrays.stream(departments)
                .collect(Collectors.toMap(String::hashCode, str -> str));
//        System.out.println(map11);


        // 集合合并
        List<Pair<String, Double>> pairArrayList = new ArrayList<>(3);
        pairArrayList.add(new Pair<>("Version", 12.10));
        pairArrayList.add(new Pair<>("Version", 12.19));
        pairArrayList.add(new Pair<>("Version", 6.28));

        // 生成的 map 集合中只有一个键值对：{Version}
        Map<String, Double> map = pairArrayList.stream()
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue, (v1, v2) -> v2));

        map.keySet();

        Collections.emptyList();

        map.entrySet();


        String[] str = new String[]{ "yang", "guan", "bao" };
        List list = Arrays.asList(str);

        System.out.println(map);

//        String[] departments = new String[]{"RDC", "RDC", "KKB"};
//        // 抛出IllegalStateException异常
//        Map<Integer, String> map2 = Arrays.stream(departments)
//                .collect(Collectors.toMap(String::hashCode, , (str1,str2) -> str1));
        List<String> list1 = new ArrayList<>();
        list1.add("1");
        list1.add("2");

        new HashMap().forEach((k,v)->{

        });


//        new ArrayList().subList()
        for (String item : list1) {
            System.out.println(item);
            System.out.println("3".equals(item));
            if ("3".equals(item)) {
                list1.remove(item);
            }
        }
        System.out.println(list1);
    }

    public static void main3(String args[]) {
        System.out.println(WeekEnum.FRIDAY.compareTo(WeekEnum.MONDAY));
        System.out.println(WeekEnum.FRIDAY.compareTo(WeekEnum.SUNDAY));
        System.out.println(WeekEnum.FRIDAY.compareTo(WeekEnum.SATURDAY));
    }

    public static void main2(String[] args) {
        // 缩进4个空格
        String say = "hello";
        // 运算符的左右必须有一个空格
        int flag = 0;
        // 关键词if与括号之间必须有一个空格，括号内的f与左括号，0与右括号不需要空格
        if (flag == 0) {
            System.out.println(say);
        }


        // 左大括号前加空格且不换行；左大括号后换行
        if (flag == 1) {
            System.out.println("world");
        // 右大括号前换行，右大括号后有else，不用换行
        } else {
            System.out.println("ok");
            // 在右大括号后直接结束，则必须换行
        }
    }
}
