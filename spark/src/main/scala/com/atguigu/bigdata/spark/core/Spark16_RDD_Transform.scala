package com.atguigu.bigdata.spark.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark16_RDD_Transform {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local[*]").setAppName("rdd")
    val sc = new SparkContext(conf)

    val list = List(1, 2, 3, 4)
    val rdd = sc.makeRDD(list, 2)

    val numRDD = rdd.mapPartitions(
      datas => {
        datas.filter(_%2 == 0)
      }
    )

    numRDD.saveAsTextFile("output")

    sc.stop

  }

}
