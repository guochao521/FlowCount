package spark.sql

import org.apache.spark.sql.functions.{col, lit}
import org.apache.spark.sql.types.{BooleanType, DateType, DoubleType, IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

class TestStruct {

}

object TestStruct{
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("TestStruct2").setMaster("local")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val spark = new SQLContext(sc)

    val data = Seq(
      Row(Row("James ", "", "Smith"), "36636", "M", "3000"),
      Row(Row("Michael ", "Rose", ""), "40288", "M", "4000"),
      Row(Row("Robert ", "", "Williams"), "42114", "M", "4000"),
      Row(Row("Jen ", "Mary", "Brown"), "", "F", "-1")
    )

    val schema = new StructType()
      .add("name", new StructType()
        .add("firstname", StringType)
        .add("middlename", StringType)
        .add("lastname", StringType)
      )
      .add("dob", StringType)
      .add("gender", StringType)
      .add("salary", StringType)


    val df = spark.createDataFrame(spark.sparkContext.parallelize(data),schema)
    df.show()
    df.printSchema()

    /**
     * 数据类型转换
     * 1.本地类型 -> Spark类型：
     *  1) 通过本地对象创建 DataFrame：toDF()、createDataFrame();
     *  2) 将本地基本类型转化为 Spark 基本类型：lit();
     *  3) udf 返回值会被隐式地转化为 Spark 对应的类型；
     *
     * 2. Spark 类型 -> 本地类型：
     *  1）将 DataFrame 收集到 driver 端：collect();
     *  2) 向 udf 传递参数时，会将 Spark 类型隐式地转化为对应的本地类型；
     */

    df.select(lit(5).as("f_integer"), lit("five").as("f_string"), lit(5.0).as("f_double"))
    df.show()

    // 需要注意的是，如果传给 lit() 的参数本身就是 Column 对象，lit() 将原样返回该 Column 对象；


    /**
     * Spark 类型 & Spark 类型
     * 将 DataFrame 列类型从一种类型转换到另一种类型有很多种方法：withColumn()、cast()、selectExpr、SQL 表达式，
     * 需要注意的是目标类型必须是 DataType 的子类。
     */
    val simpleData = Seq(Row("James",34,"2006-01-01","true","M",3000.60),
      Row("Michael",33,"1980-01-10","true","F",3300.80),
      Row("Robert",37,"06-01-1992","false","M",5000.50)
    )

    val simpleSchema = StructType(Array(
      StructField("firstName",StringType,true),
      StructField("age",IntegerType,true),
      StructField("jobStartDate",StringType,true),
      StructField("isGraduated", StringType, true),
      StructField("gender", StringType, true),
      StructField("salary", DoubleType, true)
    ))


    val df2 = spark.createDataFrame(spark.sparkContext.parallelize(simpleData),simpleSchema)
    df2.printSchema()
    df2.show(false)

    // 通过 withColumn()、cast()
    val df3 = df2
      .withColumn("age", col("age").cast(StringType))
      .withColumn("isGraduated", col("isGraduated").cast(BooleanType))
      .withColumn("jobStartDate", col("jobStartDate").cast(DateType))

    df3.printSchema()

    // 通过 select
    println("通过 SELECT")
    val cast_df = df2.select(df2.columns.map {
      case column@"age" =>
        col(column).cast("String").as(column)
      case column@"salary" =>
        col(column).cast("String").as(column)
      case column =>
        col(column)
    }: _*)

    println("展示表结构")
    cast_df.printSchema()
    println("Show Cast_DF Data")
    cast_df.show()

    // 通过 selectExpr

    println("通过 selectExpr")

    val df4 = df2.selectExpr("cast(age as int) age",
      "cast(isGraduated as string) isGraduated",
      "cast(jobStartDate as string) jobStartDate"
    )
    df4.printSchema()
    df4.show(false)

    /**
     * 布尔类型
     * 过滤数据
     */
    println("Filter Data By Salary")
    df.where(col("salary") < 4000).show()

    // Scala 中判断列是否相等使用 ===，=!=
    df.where(col("salary") === 4000).show()

    df.where(col("salary") =!= 4000).show()

    println("Equal 400 Judge")
    df.select((col("salary") =!= 4000).as("equal_300")).show()

    df.select((col("salary") =!= 4000).as("equal_400")).printSchema()

    // 布尔表达式更简洁的表达方式是使用 SQL 表达式
    df.where("salary=4000 and gender='M'").show()
  }
}