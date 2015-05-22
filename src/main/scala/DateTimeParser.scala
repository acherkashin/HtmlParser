import java.util.regex.{Matcher, Pattern}

import org.joda.time.DateTime

object DateTimeParser {

  def parseDateTime(strDateTime: String, url: String) : String ={
    val name = selectSite(url)

    name match {
      case NameSite.habrahubr => getDateTimeForHabraHabr(strDateTime)
      case NameSite.linuxOrg => getDateTimeForLinuxOrg(strDateTime)
    }
  }
//метод, определяющий по ссылке, с помощью правил какого сайта нужно обрабатывать строку даты и времени
  def selectSite( url : String ) : NameSite = {
    if(url.indexOf("habrahabr") >= 0)
      NameSite.habrahubr
    else
      NameSite.linuxOrg
  }

  def getDateTimeForHabraHabr(strDateTime : String):String = {
    val year = getYear(strDateTime)
    var day = getDayForHabraHabr(strDateTime)
    val month = getMonthForHabraHabr(strDateTime)
    val minutes = getMinutesForHabraHabr(strDateTime)
    val hours = getHours(strDateTime)

    if(day == 0)
      day = 1

    val dateTime = new DateTime(year, month, day, hours, minutes)
    dateTime.toString
  }

  //подходит как для linux.org так и для HabraHabr
  def getYear(strDateTime : String ) : Int = {
    val regexForYear = "\\d\\d\\d\\d"
    getHoursOrMinutesOrYear( regexForYear , strDateTime , Time.year)
  }
  //подходит как для linux.org так и для HabraHabr
  def getHours(strDateTime : String) : Int = {
    val regexForHourse = "\\d*:"
    getHoursOrMinutesOrYear(regexForHourse, strDateTime, Time.hour)
  }

  def getMinutesForHabraHabr(strDateTime : String) : Int = {
    val regexForMinutes = ":\\d\\d"
    getHoursOrMinutesOrYear(regexForMinutes, strDateTime, Time.minutes)
  }
  def getMonthForHabraHabr(strDateTime : String) : Int ={
    val january = ("январ.", 1)
    val february = ("феврал.", 2)
    val march = ("март.", 3)
    val april = ("апрел", 4)
    val may = ("ма", 5)
    val june = ("июн", 6)
    val july = ("июл", 7)
    val august = ("август",8)
    val septemper = ("сентябр", 9)
    val oktober = ("октябр", 10)
    val november = ("ноябр", 11)
    val december = ("декабр",12)
    val array = Array(january, february, march, april, may, june, july, august, septemper, oktober, november, december)

    for(elem <- array){
      val matcher = makeMatcher(elem._1, strDateTime)
      if(matcher.find)
        return elem._2
    }
      return DateTime.now.monthOfYear.get
  }


//  Метод-обобщение на его основе строятся сетоды getHours, getMinutes, getYear
//  Метод-обобщение на его основе строятся сетоды getHours, getMinutes, getYear
  def getHoursOrMinutesOrYear(regex : String, strDateTime : String, time : Time): Int ={
    val matcher = makeMatcher(regex, strDateTime)
    val pageHasInformation = matcher.find
    val information = if ( pageHasInformation )
      DeleteSeparators(matcher.group).toInt
    else
      time match {
        case Time.minutes => DateTime.now.minuteOfHour.get
        case Time.hour => DateTime.now.hourOfDay.get
        case Time.year =>  DateTime.now.year.get
        case Time.month => DateTime.now.monthOfYear.get
      }

    information
  }


  def getDayForHabraHabr( strDateTime : String ): Int = {

    val strYesterday = "вчера"
    val strToday = "сегодня"

    val hasYesterday = strDateTime.indexOf(strYesterday) >= 0
    val hasToday = strDateTime.indexOf(strToday) >= 0
    val day = if(hasToday)
                 DateTime.now.dayOfMonth.get
              else if(hasYesterday)
                      DateTime.now.minusDays(1).dayOfMonth().get
                   else
                      getDayFromNumber(strDateTime)

    day
  }

  def getDateTimeForLinuxOrg(strDateTime : String) : String ={
    val year = getYear(strDateTime)
    val day = getDayFromNumber(strDateTime)
    val month = getMonthForLinuxOrg(strDateTime)
    val minutes = getMinutesForLinuxOrg(strDateTime)
    val hourse = getHours(strDateTime)

    val dateTime = new DateTime(year, month, day, hourse, minutes)
    dateTime.toString
  }


  def getMinutesForLinuxOrg(strDateTime : String) : Int = {
    val regexForMinutes = ":\\d*:"
    getHoursOrMinutesOrYear(regexForMinutes, strDateTime, Time.minutes)
  }

  def getMonthForLinuxOrg(strDateTime : String) : Int = {
    val regexForMinutes = "[.]\\d*[.]"
    getHoursOrMinutesOrYear(regexForMinutes, strDateTime, Time.month)
  }

private  def makeMatcher(regex : String , pattern : String): Matcher ={
    val patternForTime = Pattern.compile(regex)
    val matcherForDateTime = patternForTime.matcher(pattern)

    matcherForDateTime
  }

private  def DeleteSeparators(str : String) : String = {
    var newStr = new StringBuffer(str)
    delColon(newStr)
    delDots(newStr)

    newStr.toString
  }

private  def delDots(strBuf : StringBuffer): StringBuffer = {

    var hasDot = true
    while(hasDot){
      val index = strBuf.indexOf(".")
      if(index >= 0) {
        strBuf.deleteCharAt(index)
      }
      else
        hasDot = false
    }

    strBuf
  }

private  def delColon(strBuf : StringBuffer): StringBuffer ={
    var hasColon = true
    while(hasColon){
      val index = strBuf.indexOf(":")
      if(index >= 0) {
        strBuf.deleteCharAt(index)
      }
      else
        hasColon = false
    }
    strBuf
  }

  def getDayFromNumber(strDateTime: String) : Int = {
    val regexForDay = makeMatcher("[0-9]+", strDateTime)
    val startWithNumber = regexForDay.find

    val day = if(startWithNumber)
                regexForDay.group.toInt
              else
                DateTime.now.dayOfMonth.get
    day
  }
}
