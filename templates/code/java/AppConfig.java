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
