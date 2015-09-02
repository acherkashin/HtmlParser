package ParsDateTime

import org.joda.time.DateTime

/**
 * Created by Александр on 02.09.2015.
 */
class LinuxOrgDateTime extends BaseDateTime{
  def getYear: Int = {
    val regex = "\\d\\d\\d\\d"
    val matcher = makeMatcher(regex, strDateTime)

    if ( matcher.find )
      matcher.group.toInt
    else
      throw  new IllegalArgumentException(s"LinuxOrg. Incorrect format of Year: $strDateTime")
  }

  def getMonth: Int = {
    val regex = "[.]\\d*[.]"
    val matcher = makeMatcher(regex, strDateTime)

    if ( matcher.find )
      delDots(matcher.group).toInt
    else
      throw  new IllegalArgumentException(s"LinuxOrg. Incorrect format of Month: $strDateTime")
  }

  def getMinutes: Int = {
    val regexForDay = makeMatcher(":\\d*:", strDateTime)

    if(regexForDay.find)
      delColon(regexForDay.group).toInt
    else
      throw  new IllegalArgumentException(s"LinuxOrg. Incorrect format of Minute: $strDateTime")
  }

  def getHours: Int = {
    val regexForDay = makeMatcher("\\d+:", strDateTime)

    if(regexForDay.find)
      delColon(regexForDay.group).toInt
    else
      throw  new IllegalArgumentException(s"LinuxOrg. Incorrect format of Hour: $strDateTime")
  }

  def getDay: Int = {
    val regexForDay = makeMatcher("\\d+", strDateTime)
    val startWithNumber = regexForDay.find

    if(startWithNumber)
      regexForDay.group.toInt
    else
      throw  new IllegalArgumentException(s"LinuxOrg. Incorrect format of Hour: $strDateTime")
  }
}
