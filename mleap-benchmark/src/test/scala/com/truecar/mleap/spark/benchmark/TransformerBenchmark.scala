package com.truecar.mleap.spark.benchmark

import java.io.{FileInputStream, File}

import ml.bundle.fs.DirectoryBundle
import com.truecar.mleap.runtime.LocalLeapFrame
import com.truecar.mleap.runtime.transformer.Transformer
import com.truecar.mleap.serialization.ml.v1.MlJsonSerializer
import org.scalameter.api._
import org.scalameter.picklers.Implicits._
import spray.json._
import com.truecar.mleap.serialization.mleap.v1.MleapJsonSupport._

/**
  * Created by hwilkins on 2/23/16.
  */
object TransformerBenchmark extends Bench.ForkedTime {
  lazy override val executor = {
    SeparateJvmsExecutor(
      Executor.Warmer.Zero,
      Aggregator.min[Double],
      new Measurer.Default)
  }

  val mlSerializer = MlJsonSerializer
  val classLoader = getClass.getClassLoader
  val regressionFile = new File("/tmp/transformer.ml")
  val frameFile = new File("/tmp/frame.json")

  val bundleReader = DirectoryBundle(regressionFile)
  val regression = mlSerializer.deserializeWithClass(bundleReader).asInstanceOf[Transformer]

  val lines = scala.io.Source.fromFile(frameFile).mkString
  val frame = lines.parseJson.convertTo[LocalLeapFrame]

  val ranges = for {
    size <- Gen.range("size")(1000, 10000, 1000)
  } yield 0 until size

  measure method "transform" in {
    using(ranges) in {
      size =>
        size.foreach {
          _ => regression.transform(frame)
        }
    }
  }
}
