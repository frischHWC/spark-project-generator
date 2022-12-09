/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package {{ package_name }}

{% if logger is sameas true %}import org.apache.log4j.Logger{% endif %}
{% if "core" is in feature %}import org.apache.spark.SparkContext{% endif %}
{% if "sql" is in feature %}import org.apache.spark.sql.SparkSession{% endif %}
{% if "streaming" is in feature %}import org.apache.spark.streaming.StreamingContext{% endif %}


object Treatment {

  {% if logger is sameas true %}@transient lazy val logger = Logger.getLogger(getClass.getName){% endif %}

  {% if "core" is in feature %}/**
    * Spark Simple Treatment
    */
  def treatment(sc: SparkContext): Unit = {

    // TODO : Replace lines belows by your code using the SparkContext passed in argument

    val rdd = sc.parallelize(Array(0,1,2,3,4,5))
    {% if logger is sameas true %}rdd.foreach(x => {logger.info("x * 2 = " + x*2)}){% endif %}
    {% if logger is sameas true %}logger.info("Mean is : " + rdd.mean().toString){% endif %}

  } {% endif %}

  {% if "sql" is in feature %}/**
    * Spark SQL treatment
    */
  def sqlTreatment(spark: SparkSession): Unit = {

  // TODO : Replace lines belows by your code using the SparkSession passed in argument
  val dfInit = spark.read
    .option("sep", ",")
    .option("inferSchema", "true")
    .option("header", "true")
    .csv(AppConfig.hdfs + AppConfig.hdfsHomeDir + "random-data.csv")

    dfInit.show(false)

  } {% endif %}

  {% if "structured_streaming" is in feature %}/**
    * Spark Structured Streaming Treatment
    */
  def structuredStreamingTreatment(spark: SparkSession): Unit = {

    // TODO : Replace lines belows by your code using the SparkSession passed in argument

    val dfStreamed = spark.readStream
      .parquet(AppConfig.hdfs + AppConfig.hdfsHomeDir)

    dfStreamed.writeStream.format("parquet")
      .option("checkpointLocation", AppConfig.hdfs + AppConfig.hdfsHomeDir + "checkpoints/")
      .option("path", AppConfig.hdfs + AppConfig.hdfsHomeDir + "random-data-2.parquet")
      .start()

    spark.streams.awaitAnyTermination()

  }{% endif %}

    {% if "streaming" is in feature %}/**
    * Spark streaming treatment to enrich data
    */
  def streamingTreatment(ssc: StreamingContext): Unit = {

   // TODO : Replace lines belows by your code using the SparkSession passed in argument
   val streamfile = ssc.textFileStream(AppConfig.hdfs + AppConfig.hdfsHomeDir + "streaming/")

    streamfile.foreachRDD(rdd => {
      rdd.foreach(record => {
        logger.info("Record is : " + record)
      })
    })


  }{% endif %}

}
