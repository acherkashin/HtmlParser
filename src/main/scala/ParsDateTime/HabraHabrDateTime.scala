package ParsDateTime

import WorkWithHtml.Time
import org.joda.time.DateTime

/**
 * Created by Александр on 01.09.2015.
 */
class HabraHabrDateTime extends BaseDateTime{

  private var backDay = false

  override def getDateTime : String = {
    getDay
    val dt = if(backDay) {
               val numberYesterday = DateTime.now.minusDays(1).dayOfMonth.get
               val numberToday = DateTime.now.dayOfMonth.get
               val backMonth = if ( numberYesterday > numberToday)
                                 DateTime.now.minusMonths(1).monthOfYear.get
                               else
                                 DateTime.now.monthOfYear.get
               val thisMonth : Int = DateTime.now.monthOfYear.get

               val year = if( thisMonth < backMonth )
                            DateTime.now.minusYears(1).year.get
                          else
                            DateTime.now.year.get

      new DateTime(year, backMonth, numberYesterday, getHours, getMinutes )
             }else
                new DateTime(getYear, getMonth, getDay, getHours, getMinutes )
    dt.toString
  }

  def getYear : Int = {
    val matcher = makeMatcher("\\d\\d\\d\\d", strDateTime)
    if ( matcher.find )
      delDots(matcher.group).toInt
    else
      DateTime.now.year.get
  }

  def getMonth: Int ={
    val arrayMonths = createArrayOfMonth
    var month = 0

    for( elem <-arrayMonths if(month == 0) ){
      val matcher = makeMatcher(elem._1, strDateTime)
      if( matcher.find )
        month = elem._2
    }
    if(month != 0)
      month
    else
      DateTime.now.monthOfYear.get
  }

  def getMinutes : Int = {
    val matcher = makeMatcher(":\\d\\d", strDateTime)

    if ( matcher.find )
      delColon(matcher.group).toInt
    else
      throw new  IllegalArgumentException(s"HabraHabr. Incorrect format of Minute: $strDateTime")
  }

  def getHours : Int = {
    val matcher = makeMatcher("\\d*:", strDateTime)
    if (matcher.find)
      delColon(matcher.group).toInt
    else
      throw new  IllegalArgumentException(s"HabraHabr. Incorrect format of Hours: $strDateTime")
  }
  def getDay: Int = {
    backDay = false
    val strYesterday = "вчера"
    val strToday = "сегодня"

    val hasYesterday = strDateTime.indexOf(strYesterday) >= 0
    val hasToday = strDateTime.indexOf(strToday) >= 0
    val day = if(hasToday)
      DateTime.now.dayOfMonth.get
    else if(hasYesterday) {
      backDay = true
      DateTime.now.minusDays(1).dayOfMonth().get
    }
    else
      getDayFromNumber

    day
  }

  private def createArrayOfMonth : Array[(String, Int)] = {
    val january = ("январ.", 1)
    val february = ("феврал.", 2)
    val march = ("март.", 3)
    val april = ("апрел", 4)
    val may = ("ма", 5)
    val june = ("июн", 6)
    val july = ("июл", 7)
    val august = ("август",8)
    val september = ("сентябр", 9)
    val october = ("октябр", 10)
    val november = ("ноябр", 11)
    val december = ("декабр",12)
    Array(january, february, march, april, may, june, july, august, september, october, november, december)
  }

  private def getDayFromNumber : Int = {
    val regexForDay = makeMatcher("[0-9]+", strDateTime)
    val startWithNumber = regexForDay.find

    val day = if(startWithNumber)
      regexForDay.group.toInt
    else
      DateTime.now.dayOfMonth.get
    day
  }
}
