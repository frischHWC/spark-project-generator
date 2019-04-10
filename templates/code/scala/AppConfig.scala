package {{ package_name }}

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory


object AppConfig {

  val conf: Config = ConfigFactory.load()

  val name = conf.getString("appName")
  val master = conf.getString("master")

  val hdfs = conf.getString("hdfs.url")
  val hdfsHomeDir = conf.getString("hdfs.home_dir")

  {% if feature is not none and "streaming" is in feature %}val streamingTime = conf.getInt("streamingTime"){% endif %}
}
