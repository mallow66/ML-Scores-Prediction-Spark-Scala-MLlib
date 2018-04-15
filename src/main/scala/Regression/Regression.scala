package Regression

import MLConfiguration.MLConfig
import SparkConfiguration.MySparkConfig
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.{LabeledPoint, LinearRegressionModel, LinearRegressionWithSGD}
import org.apache.spark.rdd.RDD
/**
  * Created by brahim on 4/10/18.
  */

object Regression extends App with MySparkConfig{


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
      val doubleArray: Array[Double] = arrayToDouble(line.split(","))
      LabeledPoint(doubleArray(36), Vectors.dense(doubleArray.take(35)))
    })

      val dataVisitors = rdd.map(line =>{
        val doubleArray: Array[Double] = arrayToDouble(line.split(","))
        LabeledPoint(doubleArray(35), Vectors.dense(doubleArray.take(35)))
      })



      (dataHome, dataVisitors)
  }

  def trainMyData(data: (RDD[LabeledPoint], RDD[LabeledPoint]), numIterations: Int, stepSize: Double)
  :(LinearRegressionModel, LinearRegressionModel, (Double, Double)) = {
    val homeModel: LinearRegressionModel = LinearRegressionWithSGD.train(data._1, numIterations, stepSize)
    val visitorsModel = LinearRegressionWithSGD.train(data._2, numIterations, stepSize)
    val vp = valuesAndPreds(data, (homeModel, visitorsModel))
    val sqErrors = squareErrors(vp)

    (homeModel, visitorsModel, sqErrors)
  }

  def valuesAndPreds(data: (RDD[LabeledPoint], RDD[LabeledPoint]), models: (LinearRegressionModel, LinearRegressionModel)):
  (RDD[(Double, Double)], RDD[(Double, Double)])= {


    val valuesAndPredHome = data._1.map(point => {
      val prediction = models._1.predict(point.features)
      (point.label, prediction)
    })

    val valuesAndPredVisitors = data._2.map(point => {
      val prediction = models._2.predict(point.features)
      (point.label, prediction)
    })

    (valuesAndPredHome, valuesAndPredVisitors)
  }


  def squareErrors(valuesAndPreds: (RDD[(Double, Double)], RDD[(Double, Double)])): (Double, Double) = {

    val MSEHome = valuesAndPreds._1.map{ case(v, p) => math.pow((v - p), 2) }.mean()

    val MSEVisitors = valuesAndPreds._2.map{ case(v, p) => math.pow((v - p), 2) }.mean()

    (MSEHome, MSEVisitors)

  }


  def teamsRegression(fileName: String): (LinearRegressionModel, LinearRegressionModel, (Double, Double)) = {

    val rdd = fromCSVToRDD(fileName)
    val data = trainingDataHomeAndVisitors(rdd)

    trainMyData(data, MLConfig.NUM_ITERATIONS, MLConfig.STEP_SIZE)
  }


















}
