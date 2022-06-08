package hive;

import utils.Logger;

import java.sql.*;

public class HiveTest implements Logger {

    private static String driverName = "org.apache.hive.jdbc.HiveDriver";

    public static void main(String[] args) throws SQLException{
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e){
            e.printStackTrace();
            System.exit(1);
        }

        Connection conn = DriverManager.getConnection("jdbc:hive2://localhost:port/","hadoop","");
        Statement stmt = conn.createStatement();

        String tableName = "HiveTestByJava";

        String dropTable = "drop table if exists " + tableName;
        stmt.execute(dropTable);

        String createTable = "create table " + tableName + "(key int, value string)";
        // 创建hive表
        stmt.execute(createTable);
        logger.info(String.format("Create table %s success!", tableName));

        // Show tables
        String showSQL = "show tables " + tableName;
        logger.info("Running：" + showSQL);
        ResultSet res = stmt.executeQuery(showSQL);
        if (res.next()){
            logger.info("Tables: " + res.getString(1));
        }

        // describe table
        String desSQL = "describe " + tableName;
        logger.info("Running: " + desSQL);
        ResultSet desRes = stmt.executeQuery(desSQL);
        while (desRes.next()){
            logger.info("Describe: " + res.getString(1) + "\t" + res.getString(2));
        }

        // Insert Table
        String insertSQL = "insert into " + tableName + "values(42, \"hello\"),(48,\"world\")";
        stmt.execute(insertSQL);

        // Select
        String selectSQL = "select * from " + tableName;
        logger.info("Running: " + selectSQL);
        res = stmt.executeQuery(selectSQL);
        while (res.next()){
            logger.info(String.valueOf(res.getInt(1) + "\t" + res.getString(2)));
        }

        // Count
        String countSQL = "select count(1) from " + tableName;
        logger.info("Running: " + countSQL);
        res = stmt.executeQuery(countSQL);
        while (res.next()){
            logger.info(res.getString(1));
        }
    }
}
