#!/usr/bin/env bash
spark-submit \
    --class {{ package_name }}.App \
    --master {{ master }} \
    --deploy-mode cluster \{% if (language == "scala" or language == "java") and logger %}
    --files log4j.properties \
    --conf "spark.executor.extraJavaOptions=-Dlog4j.configuration=log4j.properties" \
    --conf "spark.driver.extraJavaOptions=-Dlog4j.configuration=log4j.properties" \{% endif %}{% if kerberos is sameas true %}
    --principal {{ principal }} \
    --keytab {{ keytab }} \{% endif %}{% if language == "scala" or language == "java" %}
    {{ project_name }}-0.1-SNAPSHOT-jar-with-dependencies.jar -Dconfig.file=application.conf {% endif %}