package spark.udf

import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.functions.udf
import org.apache.spark.{SparkConf, SparkContext}

/**
 * 参考链接：https://www.cnblogs.com/yyy-blog/p/10280657.html
 */

object TestDataUDF {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("TestDataUDF").setMaster("local")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val spark = new SQLContext(sc)

    // 构建测试数据，有两个字段：名字和年龄
    val userData = Array(("A", 16), ("B", 21), ("B", 14), ("B", 18))

    // 创建测试df
    val userDF = spark.createDataFrame(userData).toDF("name", "age")

    userDF.show()

    // 注册一张user表
    userDF.createOrReplaceTempView("user")
//    userDF.registerTempTable("user") // spark 1.0

    // 1. 注册
    // 方法一： 匿名函数
    val strLen = udf((str: String) => str.length())

    // 方法二：通过实名函数
    spark.udf.register("isAdult", isAdult _)

    val udf_isAdult = udf(isAdult _)

    // 2.使用
    // 通过 withColumn 添加列
    userDF.withColumn("name_len", strLen(col("name"))).withColumn("isAdult", udf_isAdult(col("age"))).show()

    //通过select添加列
    userDF.select(col("*"), strLen(col("name")) as "name_len", udf_isAdult(col("age")) as "isAdult").show

  }

  def isAdult(age : Int): Boolean ={
    if(age < 18) {
      false
    } else {
      true
    }
  }
}
