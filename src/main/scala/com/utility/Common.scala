package com.utility

/**
 * Created by oshikawatakashi on 2015/08/13.
 */
trait Common {

  lazy val square: Int => Int = n => n*n
  lazy val div2: Int => Int = n => n/2
  lazy val add: (Int, Int) => Int = (n1, n2) => n1+n2
  def addDiv = div2 compose square

  //JSON形式で関数を返す
  //Mapを１つ取得してkeyとvalueをjson形式にしてStringで返す
  def listConvertToJson[A,B](mp: Map[A,B]): String =
    mp.foldLeft("")((k,v) => k + jsonFormat(v._1, v._2) )

  //入力された2つの値をJSON形式にして返す
  lazy val jsonFormat: (Any, Any) => String = (val1, val2) =>
    "\"" + val1 + "\":\"" + val2 + "\","

}