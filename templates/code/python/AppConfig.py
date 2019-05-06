
# TODO : Add other configurations properties here
app_name = "spark_py"
master = "yarn"
{% if "streaming" or "sql" or "structured_streaming" is in feature %}
hdfs = "hdfs://frisch-ubu-1.vpc.cloudera.com:8020"
hdfs_home_dir = "/user/fri/"{% endif %}
{% if "streaming" is in feature %}streaming_time = 5{% endif %}