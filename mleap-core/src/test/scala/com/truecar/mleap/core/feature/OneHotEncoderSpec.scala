package com.truecar.mleap.core.feature

import org.scalatest.FunSpec

/**
  * Created by hwilkins on 1/21/16.
  */
class OneHotEncoderSpec extends FunSpec {
  describe("#apply") {
    it("encodes the value as a vector") {
      val encoder = OneHotEncoder(5)

      assert(encoder(1.0).toArray.sameElements(Array(0.0, 1.0, 0.0, 0.0, 0.0)))
      assert(encoder(3.0).toArray.sameElements(Array(0.0, 0.0, 0.0, 1.0, 0.0)))
    }
  }
}
