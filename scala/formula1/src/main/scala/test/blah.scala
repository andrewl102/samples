package test

import spire.math.Rational

/**
 * Created by andrewlynch on 3/11/15.
 */
object blah extends App{
  val big = BigDecimal(0.000001)/BigDecimal(3234234234234.623567445445334534534545)*BigDecimal(3234234234234.623567445445334534534545)
  println(big)
  val big2 = BigDecimal(0.000)
  println(big2)

//  val frac = new Fractional[Int]
  val rat1 = Rational(1000, 32342334534534545L) / Rational(32342334534534545L) * Rational(32342334534534545L) * Rational(32342334534534545L)
  println(rat1)
}
