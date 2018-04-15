package PreProcessing

import java.io.{File, FileWriter}

import MLConfiguration.MLConfig

import scala.io.Source.fromFile

/**
  * Created by brahim on 4/8/18.
  */
object DateAndTime extends App{


  def toTimeFeatures(inputFile: String, outputFile: String): Unit = {

    val fileWriter = new FileWriter(new File(outputFile))
    val inFile = fromFile(inputFile)

    val iterator: Iterator[String] = inFile.getLines()

    val firstLine: Array[String] = iterator.next().split(",")
    val left = firstLine.take(30)
    fileWriter.append( left.mkString("", ",",","))
    fileWriter.append("Day of Week, Month,Day of Month, Year,")
    val rest = firstLine.takeRight(5)
    fileWriter.append(rest.mkString("", ",","")+"\n")

    while(iterator hasNext){
      val line = iterator.next().split(",")
      val left = line.take(30)
      val rest = line.takeRight(4)
      val date: Array[String] = line(30).split(" ")
     // println( line(30))
      val startTime = line(31)

      val dayOfWeek: Int =  date(0) match {
        case "Mon" => MLConfig.MONDAY
        case "Tue" => MLConfig.TUESDAY
        case "Wed" => MLConfig.WEDNSDAY
        case "Thu" => MLConfig.THURSDAY
        case "Fri" => MLConfig.FRIDAY
        case "Sat" => MLConfig.SATURDAY
        case "Sun" => MLConfig.SUNDAY
        case _ => 0
      }
      val month: Int = date(1) match {
        case "Jan" => MLConfig.JAN_MONTH
        case "Feb" => MLConfig.FEB_MONTH
        case "Mar" => MLConfig.MAR_MONTH
        case "Apr" => MLConfig.APR_MONTH
        case "May" => MLConfig.MAI_MONTH
        case "Jun" => MLConfig.JUN_MONTH
        case "Jul" => MLConfig.JUL_MONTH
        case "Aou" => MLConfig.AOU_MONTH
        case "Sep" => MLConfig.SEP_MONTH
        case "Oct" => MLConfig.OCT_MONTH
        case "Nov" => MLConfig.NOV_MONTH
        case "Dec" => MLConfig.DEC_MONTH
        case _ => 0
      }
      val dayOfMonth = date(2)

      val year = date(3) match {
        case "2014" => MLConfig.YEAR_2014
        case "2015" => MLConfig.YEAR_2015
        case "2016" => MLConfig.YEAR_2016
        case "2017" => MLConfig.YEAR_2017
        case "2018" => MLConfig.YEAR_2018
        case "2019" => MLConfig.YEAR_2019
        case _ => 0
      }

      val featureStartTime = startTime match {
        case TeamsTime(h, m, p) => if( m equals("30")) h+".50" else h+".00"
        case _ => "0"
      }


      fileWriter.append(left.mkString("",",",","))
      fileWriter.append(dayOfWeek+","+month+","+dayOfMonth+","+year)
      fileWriter.append(","+ featureStartTime)
      fileWriter.append(rest.mkString(",",",","")+"\n")


    }


    fileWriter close


  }

  toTimeFeatures("outputs.csv", "out.csv")



}
