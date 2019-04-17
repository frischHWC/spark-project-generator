from pyspark import SparkContext, SparkConf
from Treatment import treatment
from AppConfig import *


def main():
    conf = SparkConf().setAppName(app_name).setMaster(master)
    {% if "core" is in feature %}sc = SparkContext(conf=conf)
    treatment(sc){% endif %}


if __name__ == "__main__":

    main()
