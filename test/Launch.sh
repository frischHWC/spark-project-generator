#!/usr/bin/env bash

# Goal of this is to setup a complete test
#export HOST=
#export NAMESERVICE=
#export USER=
#export HDFS_WORK_DIR=


# Generate docs
python3 main.py \
    --version 2.4.0.7.1.1.0-565 \
    --master yarn \
    --language scala \
    --projectName spark_test \
    --packageName com.cloudera.frisch \
    --compilation true \
    --compiler maven \
    --sendFiles true \
    --kerberos true \
    --principal  dev \
    --keytab /home/dev/dev.keytab \
    --feature sql \
    --host ${HOST} \
    --user ${USER} \
    --hdfsNameservice ${NAMESERVICE} \
    --hdfsWorkDir ${HDFS_WORK_DIR}

# Send data files on cluster
scp ${PWD}/test/random-data.* ${USER}@${HOST}:~/spark_test/

# Prepare files in HDFS
ssh ${USER}@${HOST} "hdfs dfs -mkdir -p ${HDFS_WORK_DIR}/streaming/"
ssh ${USER}@${HOST} "hdfs dfs -put ~/spark_test/random-data.* ${HDFS_WORK_DIR}"
ssh ${USER}@${HOST} "hdfs dfs -put ~/spark_test/random-data.parquet ${HDFS_WORK_DIR}/streaming/"

# Launch treatment from here
echo "********** Launching Spark Treatment **********"
ssh ${USER}@${HOST} "cd ~/spark_test/; ./spark-submit.sh"
