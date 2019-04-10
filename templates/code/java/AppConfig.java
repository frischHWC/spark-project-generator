package {{ package_name }};


import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
class AppConfig {

  Config config = ConfigFactory.load();

  public String name = config.getString("appName");
  public String master = config.getString("master");

  {% if feature is not none and "streaming" is in feature %}public Integer streamingTime = config.getInt("streamingTime");{% endif %}

  public String hdfs = config.getString("hdfs.url");
  public String hdfsHomeDir = config.getString("hdfs.home_dir");

}
