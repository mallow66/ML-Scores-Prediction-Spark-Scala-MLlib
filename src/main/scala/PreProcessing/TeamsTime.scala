package PreProcessing

/**
  * Created by brahim on 4/9/18.
  */
object TeamsTime {

  def unapply(str: String): Option[(String, String, String)] ={

    val parts = str split(" ")
    val h = parts(0).split(":")
    val hours = h(0)
    val minutes = h(1)
    if(!parts.isEmpty && hours!=null && minutes != null)
      Some((hours, minutes, parts(1)))
    else None
  }

  def apply(hours: String, minutes: String, amOrPm: String): String = {

    hours +":"+minutes+" "+amOrPm
  }


}

