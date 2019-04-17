{% if "core" is in feature %}
def treatment(sc):
    """
    Simple treatment on Spark
    :param sc:
    :return:
    """
    rdd = sc.parallelize([1, 2, 3, 4, 5])

    rdd.foreach(lambda x: print("x * 2 = ", x*2))
    {% endif %}

