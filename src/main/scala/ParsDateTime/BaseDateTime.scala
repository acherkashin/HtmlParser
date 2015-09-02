package ParsDateTime

import java.util.regex.{Pattern, Matcher}

import org.joda.time.DateTime

/**
 * Created by Александр on 31.08.2015.
 */
abstract class BaseDateTime {
  var strDateTime = ""

  def setStrDateTime(strDateTime : String): BaseDateTime = {
    this.strDateTime = strDateTime
    this
  }

  def getYear : Int
  def getMonth : Int
  def getDay : Int
  def getHours : Int
  def getMinutes : Int

  protected def makeMatcher(regex : String , sample : String): Matcher = Pattern.compile(regex).matcher(sample)

  def getDateTime: String =  new DateTime(getYear, getMonth, getDay, getHours, getMinutes ).toString

  protected  def delDots(str : String): String = deleteSymbol(str, ".")

  protected def delColon(str : String): String = deleteSymbol(str, ":")

  protected def deleteSymbol(str: String, symbol : String): String = {
    val strBuf = new StringBuffer(str)
    var hasColon = true

    while(hasColon){
      val index = strBuf.indexOf(symbol)
      if(index >= 0) {
        strBuf.deleteCharAt(index)
      }
      else
        hasColon = false
    }
    strBuf.toString
  }
}

object BaseDateTime {
  def recognitionWebSite(strURL : String) : BaseDateTime = {
    val geekBrains = ("geekbrains", new GeekBrainsDateTime)
    val fourPDA = ("4pda", new FourPDADateTime)
    val habrahabr = ("habrahabr", new HabraHabrDateTime)
    val linuxOrg = ("linux", new LinuxOrgDateTime)

    val array = Array(geekBrains, fourPDA, habrahabr, linuxOrg)
    var dateTime : BaseDateTime = null
    for (elem <- array if(dateTime == null))
      if( strURL.contains(elem._1) )
        dateTime = elem._2

    if(dateTime != null)
      dateTime
    else
      throw new IllegalArgumentException(s"Failed recognition of site: $strURL")
  }
}
