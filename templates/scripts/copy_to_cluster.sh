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
export HOST={{ host }}
export USER={{ user }}
export DIRECTORY_TO_WORK={{ project_name }}/

# Create directory folder on cluster
ssh ${USER}@${HOST} mkdir -p ${DIRECTORY_TO_WORK}

# Copy files to cluster
scp spark-submit.sh ${USER}@${HOST}:~/${DIRECTORY_TO_WORK}
ssh ${USER}@${HOST} chmod 775 ${DIRECTORY_TO_WORK}spark-submit.sh
{% if language == "scala" or language == "java" %}
scp src/main/resources/application.conf ${USER}@${HOST}:~/${DIRECTORY_TO_WORK} {% if logger %}
scp src/main/resources/log4j.properties ${USER}@${HOST}:~/${DIRECTORY_TO_WORK} {% endif %}
scp target/{% if compiler=="sbt" %}scala-2.11/{% endif %}{{ project_name }}-0.1-SNAPSHOT-jar-with-dependencies.jar ${USER}@${HOST}:~/${DIRECTORY_TO_WORK}/{{ project_name }}.jar
{% elif language == "python" %}
zip python_files.zip *.py
scp python_files.zip ${USER}@${HOST}:~/${DIRECTORY_TO_WORK}
scp App.py ${USER}@${HOST}:~/${DIRECTORY_TO_WORK}
{% endif %}
# Check everything is in target directory
ssh ${USER}@${HOST} ls -ali ${DIRECTORY_TO_WORK}