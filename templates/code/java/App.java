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
