package WorkWithHtml

import java.util
import java.util.regex.Pattern

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

import scala.collection.mutable.ArrayBuffer

/**
 * Created by Александр on 25.08.2015.
 */
class WebSite(pages : Array[String], config: Configuration) {
  val logger = new Logger("article")
  def getUrlPage(index: Int) : String = pages(index)
  def countUrlPage(): Int = pages.length
  def getConfiguration(): Configuration = config

  def getHtmlItemFromPage(index: Int) : util.HashSet[HtmlItem] ={
    val timeOut = 15 * 1000
    val HtmlItems = new util.HashSet[HtmlItem]()
    val strURLPage = getUrlPage(index)
    val elementsWithAttr: Elements = getAllHiperLink(strURLPage)

    for ( i <- 0 until elementsWithAttr.size )                             //определяем валидность каждой ссылки
    {
      val url  = elementsWithAttr.get(i).attr("abs:href")
      try{
        if( isValidUrl(url) ){
          HtmlItems.add( HtmlItem.CreateHtmlItem(url, config.getKeyAndValueContent, config.getKeyAndValueDataTime) )
          Thread.sleep(timeOut)
        }
      }
      catch{
        case ex : Exception => logger.Error(ex)
      }
    }
    logger.Info(s"from page: $strURLPage were downloaded " + HtmlItems.size + " page")
    HtmlItems
  }

  private def AddArrayToHashSet(array : Array[HtmlItem],hashSet : util.HashSet[HtmlItem]): Unit ={
    for(item <- array)
    {
      hashSet.add(item)
    }
  }

  def getAllHtmlItems():  util.HashSet[HtmlItem] = {
    val hashSetHtmlItem = new util.HashSet[HtmlItem](0)

    for (index <- 0 until this.countUrlPage) {
      try {
        hashSetHtmlItem.addAll( getHtmlItemFromPage(index) )
      }
      catch {
        case ex: Exception => logger.Error(ex)
      }
    }
    hashSetHtmlItem
  }

  private def isInvalidUrl(url : String) : Boolean = {
    var isInvalid = false

    for(elem <- config.getinvalidParts if(isInvalid== false))
    {
      if(url.indexOf(elem) >= 0)
        isInvalid = true
    }

    isInvalid
  }


  private def satisfiesTemplate(url : String):Boolean = {

    var isHtmlItem = false

    for(elem <- config.getTemplateURL if(isHtmlItem == false))
    {
      val pattern = Pattern.compile(elem).matcher(url)
      if(pattern.find())
        isHtmlItem = true
    }

    isHtmlItem
  }


  private def isValidUrl(url: String) : Boolean ={
    var valid = false

    if( satisfiesTemplate(url) && !isInvalidUrl(url))
      valid = true

    valid
  }

  private def getAllHiperLink(urlPage: String): Elements = {
    logger.write(s"Processing page $urlPage" )                //записываем в лог
    val doc: Document = Jsoup.connect(urlPage).get

    doc.getElementsByAttribute("href")
  }

}
