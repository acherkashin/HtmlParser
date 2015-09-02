package ParsDateTime

import WorkWithHtml.Logger

/**
 * Created by Александр on 01.09.2015.
 */
class FourPDADateTime extends BaseDateTime{
  val logger = new Logger("article")

  def getMonth: Int = {
    val regex = "[.]\\d+[.]"
    val matcher = makeMatcher(regex, strDateTime)
    if( matcher.find )
      delDots(matcher.group).toInt
    else
      throw new  IllegalArgumentException(s"4pda. Incorrect format of Month: $strDateTime")
  }

  def getYear: Int = {
    2000 + strDateTime.substring(strDateTime.length - 2, strDateTime.length).toInt
  }

  def getDay: Int = {
    val regex = "\\d*\\."
    val matcher = makeMatcher(regex, strDateTime)
    if( matcher.find )
      delDots(matcher.group).toInt
    else
      throw new  IllegalArgumentException(s"4pda. Incorrect format of Day: $strDateTime")
  }

  val getHours: Int = 0

  val getMinutes: Int = 0


}
