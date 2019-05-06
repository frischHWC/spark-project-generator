{% if "core" or "streaming" is in feature %}from pyspark import SparkContext, SparkConf{% endif %}
{% if "sql" or "structured_streaming" is in feature %}from pyspark.sql import SparkSession{% endif %}
{% if "streaming" is in feature %}from pyspark.streaming import StreamingContext{% endif %}
from Treatment import *
from AppConfig import *


def main():
    {% if "core" or "streaming" is in feature %}
    conf = SparkConf().setAppName(app_name).setMaster(master)
    sc = SparkContext(conf=conf){% endif %}
    {% if "core" is in feature %}treatment(sc){% endif %}

    {% if "sql" or "structured_streaming" is in feature %}
    spark = SparkSession \
        .builder \
        .appName(app_name) \
        .config("spark.sql.streaming.schemaInference", "true") \
        .getOrCreate(){% endif %}

    {% if "sql" is in feature %}treatment_sql(spark){% endif %}
    {% if "structured_streaming" is in feature %}treatment_structured_streaming(spark){% endif %}

    {% if "streaming" is in feature %}ssc = StreamingContext(sc, streaming_time)
    treatment_streaming(ssc)
    ssc.start()
    ssc.awaitTermination(){% endif %}


if __name__ == "__main__":

    main()
