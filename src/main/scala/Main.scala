import PreProcessing.{DateAndTime, Teams}
import Regression.Regression
import SVMClassification.MySvmClassification

/**
  * Created by brahim on 4/8/18.
  */
object Main extends App{


  //giving an array of all our csv files, and the name of the output file
 Teams.teamsToFeatures(Array("data/2014-2015.csv", "data/2015-2016.csv", "data/2016-2017.csv", "data/2017-2018.csv") , "out1.csv")
  DateAndTime.toTimeFeatures("out1.csv", "out2.csv")

  val (homeModel, visitorsModel,sqErrors) = Regression.teamsRegression("out2.csv")

  println( "Home Square Error Regression: "+ sqErrors._1)
  println( "Visitor Square Error Regression: "+ sqErrors._2)

 // val (homeModelSVM, visitorsModelSVM, sqErrorsSVM) = MySvmClassification.teamsSVM("out2.csv")
  //println( "Home SVM Accuracy: "+ sqErrorsSVM._1+"%")
  //println( "Visitor Square Error SVM: "+ sqErrorsSVM._2+"%")




}
