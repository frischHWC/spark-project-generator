from AppConfig import *

{% if "core" is in feature %}
def treatment(sc):
    """
    Simple treatment on Spark
    :param sc:
    :return:
    """
    rdd = sc.parallelize([1, 2, 3, 4, 5])

    rdd.foreach(lambda x: print("x * 2 = ", x*2))
    {% endif %}

{% if "sql" is in feature %}
def treatment_sql(spark):
    """
    SQL treatment on Spark
    :param spark:
    :return:
    """
    path = hdfs + hdfs_home_dir + "random-data.csv"
    df_init = spark.read \
        .csv(path=path, header=True)

    df_init.select("name").show()
    {% endif %}

{% if "structured_streaming" is in feature %}
def treatment_structured_streaming(spark):
    """
    Structured Streaming on Spark
    :param spark:
    :return:
    """
    df_streamed = spark.readStream \
        .parquet(hdfs + hdfs_home_dir + "streaming/")

    df_streamed.writeStream.format("parquet") \
        .option("checkpointLocation", hdfs + hdfs_home_dir + "checkpoints/") \
        .option("path", hdfs + hdfs_home_dir + "random-data-2.parquet") \
        .start()

    spark.streams.awaitAnyTermination()
    {% endif %}

{% if "streaming" is in feature %}
def treatment_streaming(ssc):
    """
    Streaming on Spark
    :param ssc:
    :return:
    """
    streamfile = ssc.textFileStream(hdfs + hdfs_home_dir + "streaming/")

    def apply_to_each_row(row):
        print(row)

    def apply_to_each_rdd(rdd):
        rdd.foreach(apply_to_each_row)

    streamfile.foreachRDD(apply_to_each_rdd)

    ssc.start()
    ssc.awaitTermination()
    {% endif %}
