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
scp target/{% if compiler=="sbt" %}scala-2.11/{% endif %}{{ project_name }}-0.1-SNAPSHOT-jar-with-dependencies.jar ${USER}@${HOST}:~/${DIRECTORY_TO_WORK}
{% elif language == "python" %}
zip python_files.zip *.py
scp python_files.zip ${USER}@${HOST}:~/${DIRECTORY_TO_WORK}
scp App.py ${USER}@${HOST}:~/${DIRECTORY_TO_WORK}
{% endif %}
# Check everything is in target directory
ssh ${USER}@${HOST} ls -ali ${DIRECTORY_TO_WORK}

