package SVMClassification

import MLConfiguration.MLConfig
import Regression.Regression.{fromCSVToRDD, trainMyData, trainingDataHomeAndVisitors}
import SparkConfiguration.MySparkConfig
import org.apache.spark.mllib.classification.{SVMModel, SVMWithSGD}
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.linalg.Vectors


/**
  * Created by brahim on 4/14/18.
  */
object MySvmClassification extends App with MySparkConfig{

  def arrayToDouble(array: Array[String]): Array[Double] = {
    array map(s => s.toDouble)
  }

  def fromCSVToRDD(fileName: String): RDD[String] ={
    sc.textFile(fileName)
      . mapPartitionsWithIndex((index, iterator) => if(index == 0) iterator.drop(1) else iterator)
      .cache()
  }

  def trainingDataHomeAndVisitors(rdd: RDD[String]): (RDD[LabeledPoint], RDD[LabeledPoint]) = {
    val dataHome = rdd.map(line => {
      val tab = line.split(",")
      LabeledPoint(tab(38).toInt, Vectors.dense(arrayToDouble(tab.take(35))))
    })

    val dataVisitor = rdd.map(line => {
      val tab = line.split(",")
      LabeledPoint(tab(37).toInt, Vectors.dense(arrayToDouble(tab.take(35))))
    })

    (dataHome, dataVisitor)
  }


  def trainMyData(data: (RDD[LabeledPoint], RDD[LabeledPoint]), numIterations: Int): (SVMModel, SVMModel, (Double, Double)) ={
    val homeModel: SVMModel = SVMWithSGD.train(data._1, numIterations)
    val visitoModel: SVMModel = SVMWithSGD.train(data._2, numIterations)

    val vp = valuesAndPreds(data, (homeModel, visitoModel))
    (homeModel, visitoModel, squareErrors(vp))
  }

  def valuesAndPreds(data: (RDD[LabeledPoint], RDD[LabeledPoint]), models: (SVMModel, SVMModel)):
  (RDD[(Double, Double)], RDD[(Double, Double)]) = {

    val valuesAndPredsHome = data._1.map(point => (point.label, models._1.predict(point.features)))
    val valuesAndPredsVisitor = data._2.map(point => (point.label, models._2.predict(point.features)))

    (valuesAndPredsHome, valuesAndPredsVisitor)
  }

  def squareErrors(vp: (RDD[(Double, Double)], RDD[(Double, Double)])): (Double, Double) = {
    val mHome = vp._1.map{ case(v, p) => if (v==p) 1 else 0 }
    val pourcentageHome = (mHome.filter(_==1).count() * 100) / vp._1.count()

    val mVisitor = vp._2.map{ case(v, p) => if (v==p) 1 else 0 }
    val pourcentageVisitor = (mVisitor.filter(n => n==1).count() * 100) / vp._2.count()
    (pourcentageHome, pourcentageVisitor)
  }

  def teamsSVM(fileName: String): (SVMModel, SVMModel, (Double, Double)) = {
    val rdd = fromCSVToRDD(fileName)
    val data = trainingDataHomeAndVisitors(rdd)

    trainMyData(data, MLConfig.NUM_ITERATIONS)

  }


}
