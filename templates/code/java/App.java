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
package {{ package_name }};

{% if logger is sameas true %}import org.apache.log4j.*;{% endif %}
{% if "core" is in feature %}
import org.apache.spark.api.java.JavaSparkContext;
{% endif %}
{% if "core" is in feature or "streaming" is in feature %}
import org.apache.spark.SparkConf;
{% endif %}
{% if "streaming" is in feature %}
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
{% endif %}
{% if "sql" is in feature or "structured_streaming" is in feature %}
import org.apache.spark.sql.SparkSession;
{% endif %}

public class App {

   {% if logger is sameas true %}private static Logger logger = Logger.getLogger(App.class);{% endif %}
   private static AppConfig appConfig = new AppConfig();

  /**
    * Main function that creates a SparkContext and launches treatment
    */
  public static void main(String[] args) {

    {% if "core" is in feature %}// Creating Spark context
    SparkConf conf = new SparkConf().setMaster(appConfig.master)
      .setAppName(appConfig.name);
    JavaSparkContext sc = new JavaSparkContext(conf);

    // Launch treatment
    Treatment.treatment(sc); {% endif %}

    {% if "sql" is in feature or "structured_streaming" is in feature %}// Create Spark SQL Context
    SparkSession spark = SparkSession
      .builder()
      .appName(appConfig.name)
       {% if "structured_streaming" is in feature %}.config("spark.sql.streaming.schemaInference", "true"){% endif %}
      .getOrCreate();

     // Launch treatment
    {% if "structured_streaming" is in feature %}Treatment.structuredStreamingTreatment(spark);{% else %}Treatment.sqlTreatment(spark);{% endif %}{% endif %}

    {% if "streaming" is in feature %}
    // Create Streaming context

     SparkConf conf = new SparkConf().setMaster(appConfig.master)
      .setAppName(appConfig.name);
    JavaStreamingContext ssc = new JavaStreamingContext(conf, Durations.seconds(appConfig.streamingTime));

    // Launch Treatment
    Treatment.streamingTreatment(ssc);

    // Launch computation
    ssc.start(); // Start the computation
    try {
      ssc.awaitTermination();  // Wait for the computation to terminate
    } catch(InterruptedException e) {
      logger.error("An error occurred while making a streaming treatment");
    } {% endif %}


  }

}
