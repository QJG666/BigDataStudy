package com.atguigu.bigdata.scala.chapter07

object Scala26_Collection_Method9 {
  def main(args: Array[String]): Unit = {

    // TODO - Scala - 集合 - 常用(通用)方法

    // 排序: 升序 & 降序
    val list = List(1, 3, 2, 4)

    // println(list.reverse)
    // sortBy按照指定的规则进行排序处理
    // 这里的规则需要自行指定
    val sortList1: List[Int] = list.sortBy(num => num)
    val sortList2: List[Int] = list.sortBy(num => num)(Ordering.Int.reverse)

    println(sortList1)
    println(sortList2)

    val stringList = List("1", "2", "3", "11", "22")
    val strings: List[String] = stringList.sortBy(
      str => {
        // str.toInt
        str
      }
    )
    println(strings)

    val numList = List(1, 4, 2, 3)
    val ints: List[Int] = numList.sortWith(
      (x, y) => {
        x < y
      }
    )
    println(ints)


  }

}
