package ParsDateTime

import org.joda.time.DateTime

/**
 * Created by Александр on 31.08.2015.
 */
class GeekBrainsDateTime extends BaseDateTime{

  def this(strDateTime : String) = {
    this()
    this.strDateTime = strDateTime
  }

  def getYear : Int = {
    val matcher = makeMatcher("\\d\\d\\d\\d", strDateTime)
    if( matcher.find )
      matcher.group.toInt
    else
      throw new  IllegalArgumentException(s"GeekBrains. Incorrect format of Year: $strDateTime")
  }

  def getMonth : Int ={
    val arrayMonths = createArrayMonths

    var month = 0
    for( elem <-arrayMonths if(month == 0) ){
       if( strDateTime.indexOf(elem._1) >= 0 )
         month = elem._2
    }
    if(month != 0)
      month
    else
      throw new  IllegalArgumentException(s"GeekBrains. Incorrect format of Month: $strDateTime")
  }

  def getDay : Int = {
    val matcher = makeMatcher("\\d\\d", strDateTime)
    if( matcher.find )
      matcher.group.toInt
    else
      throw new  IllegalArgumentException(s"GeekBrains. Incorrect format of Day: $strDateTime")
  }
  val getHours : Int = 0

  val getMinutes : Int = 0

  def getStrDateTime() : String = strDateTime

  private def createArrayMonths() : Array[(String, Int)] = {
    val january = ("января", 1)
    val february = ("февраля", 2)
    val march = ("марта", 3)
    val april = ("апреля", 4)
    val may = ("мая", 5)
    val june = ("июня", 6)
    val july = ("июля", 7)
    val august = ("августа",8)
    val september = ("сентября", 9)
    val october = ("октября", 10)
    val november = ("ноября", 11)
    val december = ("декабря",12)

    Array(january, february, march, april, may, june, july, august, september, october, november, december)
  }

}
