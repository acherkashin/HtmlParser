package getterArticle

import java.io.FileWriter

import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import org.jsoup.select.Elements

import scala.Int
import scala.collection.JavaConversions._
import scala.collection.immutable.Range.Int
import scala.collection.mutable._

/**
 * Created by Александр on 15.03.2015.
 */
class HtmlParser(nameFolder : String){
  private val ID_HTMLITEM : Integer   = 1
  private val ID_INVALIDURL : Integer = 2
  private val ID_CHECKURL : Integer   = 3

  private val readerRules = new ReaderRules(nameFolder)
  private val wordsForHtmlItems = readerRules.getWordsForHtmlItems
  private val wordsForInvalidUrl = readerRules.getWordsForInvalidUrl
  private val wordsForCheckUrl = readerRules.getWordsForCheckUrl
  private val keyValue = readerRules.getKeyValue

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
      if(url.indexOf(elem) > 0 && ! url.endsWith(elem))//содержит, но не оканчивается
        isHtmlItem = true
    }

    isHtmlItem
  }

  // 1- yes , 2 - no, 3 -/
  def whatIsUrl(url: String) : Integer ={
    var id = 0

    if(! isInvalidUrl(url))
      if(isCheckUrl(url))
        if(isHtmlItem(url))
          id = ID_HTMLITEM
        else
          id = ID_CHECKURL
      else
        id = ID_INVALIDURL
    else
      id = ID_INVALIDURL

    id
  }

  def LoadHtmlItemFromPage(strUrl : String) : Array[HtmlItem] ={
    val logger = new Logger("article")
    logger.write(s"Обработка страницы $strUrl")

    val HtmlItems = ArrayBuffer[HtmlItem]()
    val CheckUrls = ArrayBuffer[String]()

    val doc: Document = Jsoup.connect(strUrl).get
    val elementsWithAttr: Elements = doc.getElementsByAttribute("href")
    val htmlItems: HashSet[HtmlItem] = new HashSet[HtmlItem]()

    val size = elementsWithAttr.size
    for ( i <- 0 until size )////определяем валидность каждой ссылки
    {
      val element = elementsWithAttr.get(i)
      val url  = element.attr("abs:href")

      val id = whatIsUrl(url)

      id match {
        case ID_HTMLITEM =>  HtmlItems += HtmlItem.CreateHtmlItem(url,keyValue)
        case ID_CHECKURL =>  CheckUrls += url
//        case ID_INVALIDURL =>  {
//                                  if(whatIsUrl(strUrl + url) == ID_HTMLITEM)
//                                    HtmlItems += HtmlItem.CreateHtmlItem(strUrl + url,keyValue)
//                                }

        case _ =>
      }


    }

    HtmlItems.toArray
  }

}
