package org.apache.spark.ml.feature

import org.apache.spark.mllib.feature.FeatureUtils.findMidPoint
import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfterAll, FunSuite}
import org.scalatest.junit.JUnitRunner

/**
  * Test static methods in FeatureUtils
  */
@RunWith(classOf[JUnitRunner])
class FeatureUtilsSuite extends FunSuite with BeforeAndAfterAll {


  test("Finding midpoint of medium numbers") {
    // these are cases that use to give too much precision
    assertResult(29.85f) { findMidPoint(29.8, 29.9)}
    assertResult(6.9625f) { findMidPoint(6.95, 6.975)}
    assertResult(0.96f) { findMidPoint(0.92, 1.0)}
  }

  test("Finding midpoints of big numbers") {
    assertResult(73.0) { findMidPoint(23, 123) }
    assertResult(73.55f) { findMidPoint(23.456, 123.654) }
    assertResult(73.61f) { findMidPoint(23.4567, 123.7654) }
    assertResult(13.555f) { findMidPoint(3.456, 23.654) }
    assertResult(123.555f) { findMidPoint(123.456, 123.654) }
    assertResult(137254.0f) { findMidPoint(129983.46, 144523.654) }
    assertResult(129303.53f) { findMidPoint(129083.46, 129523.6) }
    assertResult(4610.0f) { findMidPoint(-120303.3, 129523.6) }
    assertResult(100.0f) { findMidPoint(-2120303.3123, 2120523.68754) }
    assertResult(1.11729997E9f) { findMidPoint(0, 2234567123.45) }
  }

  test("Finding midpoints of small numbers") {
    assertResult(0.5) { findMidPoint(0, 1) }
    assertResult(-0.5) { findMidPoint(-1, 0) }
    assertResult(0.0679f) { findMidPoint(0.0123456, 0.123456) }  // 0.0679008
    assertResult(0) { findMidPoint(-0.123456, 0.123456) }
    assertResult(0.001234565f) { findMidPoint(0.00123456, 0.00123457) }
    assertResult(0f) { findMidPoint(-0.00123456, 0.00123457) }
  }
}
