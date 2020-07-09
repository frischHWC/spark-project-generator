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
