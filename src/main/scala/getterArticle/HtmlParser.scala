package getterArticle

import java.io.FileWriter
import java.util.regex.Pattern

import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import org.jsoup.select.Elements

import scala.Int
import scala.collection.JavaConversions._
import scala.collection.immutable.Range.Int
import scala.collection.mutable._
import scala.io.Source

/**
 * Created by Александр on 15.03.2015.
 */
class HtmlParser(readerConfigurations : ReaderConfigurations){
  
  //private val readerRules = new ReaderRules(nameFolder)
  private val wordsForHtmlItems = readerConfigurations.getWordsForHtmlItems
  private val wordsForInvalidUrl = readerConfigurations.getWordsForInvalidUrl
  private val wordsForCheckUrl = readerConfigurations.getWordsForCheckUrl
  private val keyValueArticle = readerConfigurations.getKeyValueArticle
  private val keyValueDateTime = readerConfigurations.getKeyValueDateTime
  private val strUrl = readerConfigurations.CurrentSite

  private def getTimeOut(url :String) : Int ={
    var timeOut = 15

    try {
      val robots = Source.fromURL(s"$url/robots.txt", "UTF-8").mkString
      val regex = "User-agent: \\W\\sCrawl-delay: \\d+"
      val pattern = Pattern.compile(regex)
      val matcher = pattern.matcher(robots)

      matcher.find()
      val str = matcher.group()

      val numberRegex = "[0-9]+"
      val numberPattern = Pattern.compile("\\d+", Pattern.MULTILINE)
      val numberMatcher = numberPattern.matcher(str)

      numberMatcher.find()
      timeOut = Integer.parseInt(numberMatcher.group())
    }
    catch {
      case _ =>
    }

    timeOut
  }

  def isInvalidUrl(url : String) : Boolean = {
    var isInvalid = false

    for(elem <- wordsForInvalidUrl if(isInvalid== false))
    {
      if(url.indexOf(elem) > 0)
        isInvalid = true
    }

    isInvalid
  }

  def isCheckUrl(url : String):Boolean = {
    var isCheckUrl = true

    for(elem<- wordsForCheckUrl if(isCheckUrl == true))
    {
      if(url.indexOf(elem) < 0)
        isCheckUrl = false
    }

    isCheckUrl
  }

  def isHtmlItem(url : String):Boolean = {
    var isHtmlItem = false

    for(elem<- wordsForHtmlItems if(isHtmlItem == false))
    {
      if(url.indexOf(elem) > 0 && ! ( url.endsWith(elem) || url.endsWith(elem + "/") ))//содержит, но не оканчивается
        isHtmlItem = true
    }

    isHtmlItem
  }

  def isValidUrl(url: String) : Boolean ={
    var valid = false

    if(! isInvalidUrl(url) && isCheckUrl(url) && isHtmlItem(url))
      valid = true

    return valid;

  }

  def LoadHtmlItemFromPage() : Array[HtmlItem] ={

    val timeOut = getTimeOut(strUrl)*1000                   //простой страницы

    val logger = new Logger("article")
    logger.write(s"Обработка страницы $strUrl")
    logger.write(s"TimeOut: $timeOut")

    val HtmlItems = ArrayBuffer[HtmlItem]()
    val doc: Document = Jsoup.connect(strUrl).get
    val elementsWithAttr: Elements = doc.getElementsByAttribute("href")

    val size = elementsWithAttr.size
    //обходим массивссылок
    for ( i <- 0 until size )////определяем валидность каждой ссылки
    {
      val element = elementsWithAttr.get(i)
      val url  = element.attr("abs:href")
      try{
        val valid = isValidUrl(url)

        if(valid){
          HtmlItems += HtmlItem.CreateHtmlItem(url, keyValueArticle, keyValueDateTime)
          //Thread.sleep(timeOut)
        }
      }
      catch{
        case e : Exception => logger.write(s"Ошибка обработки страницы:$url "+e.getMessage)
      }
    }

    HtmlItems.toArray
  }

}
