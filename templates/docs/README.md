# {{ project_name }}

This is a Spark {{ version }} project in {{ language }} {% if compiler is not none %}compiled with {{ compiler }}{% endif %}{% if feature is not none and feature|length > 0 %} with : {% for f in feature %}{{ f }}, {% endfor %}{% endif %}{% if kerberos is sameas true %} and Kerberos enabled{% endif %}.


TODO : Add description here



