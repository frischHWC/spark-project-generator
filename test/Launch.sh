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

# Goal of this is to setup a complete test
#export HOST=
#export NAMESERVICE=
#export USER=
#export HDFS_WORK_DIR=

export HOST=ccycloud-1.datagen.root.comops.site
export NAMESERVICE=datagen
export USER=root
export HDFS_WORK_DIR=/user/francois


# Generate docs
python3 main.py \
    --version 2.4.8.7.1.9.1010-2 \
    --master yarn \
    --language python \
    --projectName sample_jobs \
    --packageName com.cloudera.frisch \
    --compilation false \
    --feature sql \
    --compiler none \
    --sendFiles false \
    --kerberos true \
    --principal francois \
    --keytab /home/francois/francois.keytab \
    --host ${HOST} \
    --user ${USER} \
    --hdfsNameservice ${NAMESERVICE} \
    --hdfsWorkDir ${HDFS_WORK_DIR}

# Send data files on cluster
#scp ${PWD}/test/random-data.* ${USER}@${HOST}:~/spark_test/

# Prepare files in HDFS
#ssh ${USER}@${HOST} "hdfs dfs -mkdir -p ${HDFS_WORK_DIR}/streaming/"
#ssh ${USER}@${HOST} "hdfs dfs -put ~/spark_test/random-data.* ${HDFS_WORK_DIR}"
#ssh ${USER}@${HOST} "hdfs dfs -put ~/spark_test/random-data.parquet ${HDFS_WORK_DIR}/streaming/"

# Launch treatment from here
echo "********** Launching Spark Treatment **********"
ssh ${USER}@${HOST} "cd ~/spark_test/; ./spark-submit.sh"
