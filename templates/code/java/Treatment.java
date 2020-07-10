package {{ package_name }};

import {{ package_name }}.AppConfig;

{% if logger is sameas true %}import org.apache.log4j.Logger;{% endif %}
{% if "core" is in feature %}
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.SparkConf;
{% endif %}
{% if "streaming" is in feature %}
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
{% endif %}
{% if "structured_streaming" is in feature or "sql" is in feature %}
import org.apache.spark.sql.Row;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;
{% endif %}
{% if "structured_streaming" is in feature %}
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;
{% endif %}

import java.util.Arrays;
import java.util.List;


class Treatment {

  {% if logger is sameas true %}private static Logger logger = Logger.getLogger(Treatment.class);{% endif %}
  private static AppConfig appConfig = new AppConfig();

  {% if "core" is in feature %}/**
    * Spark Simple Treatment
    */
  public static void treatment(JavaSparkContext sc) {

  // TODO : Replace lines belows by your code using the SparkContext passed in argument

    List<Integer> data = Arrays.asList(1, 2, 3, 4, 5);
    JavaRDD<Integer> rdd = sc.parallelize(data);

    rdd.foreach(x -> logger.info("x * 2 = " + Integer.toString(x * 2)));

    logger.info("Count is : " + Long.toString(rdd.count()));

  } {% endif %}

    {% if "sql" is in feature %}/**
    * Spark SQL treatment
    */
  public static void sqlTreatment(SparkSession spark) {

  // TODO : Replace lines belows by your code using the SparkSession passed in argument

    // First goal is to load, format and write data :)
    Dataset<Row> dfInit = spark.read()
      .option("sep", ",")
      .option("inferSchema", "true")
      .option("header", "true")
      .csv(appConfig.hdfs + appConfig.hdfsHomeDir + "random-data.csv");

    dfInit.show(false);
  }{% endif %}

    {% if "structured_streaming" is in feature %}/**
    * Spark Structured Streaming Treatment
    */
  public static void structuredStreamingTreatment(SparkSession spark) {

   // TODO : Replace lines belows by your code using the SparkSession passed in argument

    // Goal is to send data to Kafka
    Dataset<Row> dfStreamed = spark.readStream()
      .parquet(appConfig.hdfs + appConfig.hdfsHomeDir + "streaming/");

    dfStreamed.writeStream()
        .option("checkpointLocation", appConfig.hdfs + appConfig.hdfsHomeDir + "checkpoints/")
        .format("csv").start(appConfig.hdfs + appConfig.hdfsHomeDir + "random-data-2.parquet");

    try {
      spark.streams().awaitAnyTermination();
    } catch (StreamingQueryException e) {
      logger.error("An error occurred while making a structured streaming treatment");
    }

  }{% endif %}

    {% if "streaming" is in feature %}/**
    * Spark streaming treatment to enrich data
    */
  public static void streamingTreatment(JavaStreamingContext ssc ){

  // TODO : Replace lines belows by your code using the SparkSession passed in argument

    JavaDStream<String> streamfile = ssc.textFileStream(appConfig.hdfs + appConfig.hdfsHomeDir + "streaming/");

    streamfile.foreachRDD(rdd -> {
      rdd.foreach(record -> {
        logger.info("Record is : " + record);
      });
    });

  } {% endif %}

}
