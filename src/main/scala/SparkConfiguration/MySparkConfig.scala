package SparkConfiguration

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by brahim on 4/3/18.
  */
trait MySparkConfig {

  private val conf = new SparkConf().setAppName("My App").setMaster("local")
  val sc =  new SparkContext(conf)

}
