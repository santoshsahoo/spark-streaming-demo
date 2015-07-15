package com.foo.datainsights

import scala.util.Random

/**
 * Created by ssahoo on 6/8/15.
 */
object Any {
  val alpha = Random.alphanumeric

  def alphaString(n:Int) = Stream.continually(alpha).take(Any.int(n)).mkString
  def int(n:Int=Integer.MAX_VALUE) = Random.nextInt(n)
  def string(n:Integer) = Random.nextString(n)
}
