package com.foo.datainsights

import scala.util.Random

/**
 * Created by ssahoo on 6/8/15.
 */
object DataGenerator {
  def string(n:Int) = Random.alphanumeric.take(n).mkString
  def int(n:Int=Integer.MAX_VALUE) = Random.nextInt(n)

}
