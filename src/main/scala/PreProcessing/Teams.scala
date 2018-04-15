package PreProcessing

/**
  * Created by brahim on 4/8/18.
  */

import java.io.{File, FileWriter}

import MLConfiguration.MLConfig

import scala.io.Source.fromFile
object Teams {


  def teamsToSet(fileNames: Array[String]): Set[String] ={
    var result = Set.empty[String]

      val file = fromFile(fileNames(0))
      val teams: List[String] = file.getLines()
        .map(line => line.split(",")(0) )
        .toList
        .tail

      result ++= teams

    result
  }

  def teamsToMap(teams: Set[String]): Map[String, Int] ={
    teams.toSeq.zipWithIndex.toMap
  }

  def teamsToFeatures( inputFiles: Array[String], outputFile: String): Unit = {
    val fileWriter = new FileWriter(new File(outputFile))
    val teamsSet = teamsToSet(inputFiles)

    fileWriter.append(teamsSet.toSeq.mkString("", ",","") + ",Date,Start (ET),PTS(Visitor),PTS(Home),Pr(Visitor), Pr(Home)")
    fileWriter.append('\n')
    for(inputFile <- inputFiles){
      val initialFile = fromFile(inputFile)
      //fileWriter close
      val iterator: Iterator[String] = initialFile.getLines()
      iterator.next()
      while (iterator hasNext){
        val line = iterator.next().split(",")
        val indexTeam1 = teamsToMap(teamsSet).get(line(0)) //the index for the visitor team
        val indexTeam2 = teamsToMap(teamsSet).get(line(1))  // the index for the home team

        val zerosArray = new Array[Int](30)
        zerosArray(indexTeam1 get) = MLConfig.VISITOR_TEAM
        zerosArray(indexTeam2 get) = MLConfig.HOME_TEAM
        fileWriter append(zerosArray mkString("", ",", ""))
        fileWriter append(line.toList.tail.tail.mkString(",",",",","))
        if(line(4).toDouble > line(5).toDouble)
          // The Visitor wins
          fileWriter append("1,0\n")
        else if(line(4).toDouble < line(5).toDouble)
          fileWriter append("0,1\n")
        else
          fileWriter append("1,1\n")


      }
    }

    fileWriter close

  }

















}
