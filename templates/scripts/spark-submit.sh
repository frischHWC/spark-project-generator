#
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#  http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
#
#!/usr/bin/env bash
{% if language == "python" %}# TODO : Change it according to your python path and which python version you want to use (or remove it if you want to use default python installed)
export PYSPARK_PYTHON=python{% endif %}
spark-submit \
    --class {% if language == "python" %}App.py{% else %}{{ package_name }}.App{% endif %} \
    --master {{ master }} \
    --deploy-mode cluster \{% if (language == "scala" or language == "java") and logger %}
    --files log4j.properties \
    --conf "spark.executor.extraJavaOptions=-Dlog4j.configuration=log4j.properties" \
    --conf "spark.driver.extraJavaOptions=-Dlog4j.configuration=log4j.properties" \{% endif %}{% if kerberos is sameas true %}
    --principal {{ principal }} \
    --keytab {{ keytab }} \{% endif %}{% if language == "scala" or language == "java" %}
    {{ project_name }}.jar -Dconfig.file=application.conf {% endif %}
    {% if language == "python" %}--py-files python_files.zip \
    App.py {% endif %}
