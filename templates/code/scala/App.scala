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

{% if logger is sameas true %}import org.apache.log4j._{% endif %}
{% if "core" is in feature %}import org.apache.spark.{SparkConf, SparkContext}{% endif %}
{% if "sql" is in feature %}import org.apache.spark.sql.SparkSession{% endif %}
{% if "streaming" is in feature %}import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.SparkConf
{% endif %}


object App {

  {% if logger is sameas true -%}@transient lazy val logger = Logger.getLogger(getClass.getName){% endif %}

  /**
   * Main function that creates a SparkContext and launches treatment
   * @param args
   */
  def main(args : Array[String]) {

    {% if "core" is in feature %} // Create Spark Context
    val conf = new SparkConf().setMaster(AppConfig.master)
      .setAppName(AppConfig.name)
    val sc = new SparkContext(conf)

    // Launch treatment
    Treatment.treatment(sc) {% endif %}

    {% if "sql" is in feature or "structured_streaming" is in feature %}// Create Spark SQL Context
    val spark = SparkSession
      .builder()
      .appName(AppConfig.name)
      {% if "structured_streaming" is in feature %}.config("spark.sql.streaming.schemaInference", "true"){% endif %}
      .getOrCreate()

     // Launch treatment
    {% if "structured_streaming" is in feature %}Treatment.structuredStreamingTreatment(spark){% else %}Treatment.sqlTreatment(spark) {% endif %}{% endif %}

    {% if "streaming" is in feature %}
    // Create Streaming context
    val conf = new SparkConf().setMaster(AppConfig.master)
      .setAppName(AppConfig.name)
    val ssc = new StreamingContext(conf, Seconds(AppConfig.streamingTime))

    // Launch Treatment
    Treatment.streamingTreatment(ssc)

    ssc.start()             // Start the computation
    ssc.awaitTermination()  // Wait for the computation to terminate {% endif %}

  }

}
