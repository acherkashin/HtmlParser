package getterArticle

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
class HtmlParser(strUrl: String){

  private val ID_HTMLITEM : Integer   = 1
  private val ID_INVALIDURL : Integer = 2
  private val ID_CHECKURL : Integer   = 3

  def isInvalidUrl(url : String) : Boolean = {
    var isInvalid = false

    for(elem <- HtmlParser.WordsForInvalidUrl if(isInvalid== false))
    {
      if(url.indexOf(elem) > 0)
        isInvalid = true
    }

    isInvalid
  }

  def isCheckUrl(url : String):Boolean = {
    var isCheckUrl = true

    for(elem<- HtmlParser.WordsForCheckUrl if(isCheckUrl == true))
    {
      if(url.indexOf(elem) < 0)
        isCheckUrl = false
    }

    isCheckUrl
  }

  def isHtmlItem(url : String):Boolean = {
    var isHtmlItem = false

    for(elem<- HtmlParser.wordsForHtmlItems if(isHtmlItem == false))
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

  def LoadHtmlItemFromPage(url : String) : (ArrayBuffer[HtmlItem], ArrayBuffer[String]) ={
    println(url)
    val HtmlItems = ArrayBuffer[HtmlItem]()
    val CheckUrls = ArrayBuffer[String]()

    val doc: Document = Jsoup.connect(url).get
    val elementsWithAttr: Elements = doc.getElementsByAttribute("href")
    val htmlItems: HashSet[HtmlItem] = new HashSet[HtmlItem]()

    val size = elementsWithAttr.size
    for ( i <- 0 until size )////определяем валидность каждой ссылки
    {
      val element = elementsWithAttr.get(i)
      val url  = element.attr("href")

      val id = whatIsUrl(url)

      id match {
        case ID_HTMLITEM =>  HtmlItems += HtmlItem.CreateHtmlItem(url)
        case ID_CHECKURL =>  CheckUrls += url
        case _ =>
      }


    }

    (HtmlItems, CheckUrls)
  }

  private def copyArrayBuffer(arrBuffer : ArrayBuffer[String]): ArrayBuffer[String] = {
    val newArrayBuffer = new ArrayBuffer[String]()

    for(elem <-newArrayBuffer){
      newArrayBuffer += new String(elem)
    }

    newArrayBuffer
  }

  def containsInArray(arrBuf : ArrayBuffer[String], url : String) : Boolean = {
    var contains = false

    for(elem <- arrBuf)
      if(url == elem)
        contains = true

    contains
  }


  def getAllArticle(rootUrl : String) : Array[HtmlItem]={
    var url = rootUrl
    val pairArray = LoadHtmlItemFromPage(url)
    val arrBufArticle  = pairArray._1 //адреса, которые нужно проверить
    val arrBufCheckUrl = pairArray._2 //посещённые адреса
    val arrBufOldUrl   = copyArrayBuffer(arrBufCheckUrl)

    while( arrBufCheckUrl.length != 0 ){
      try{
          val index = arrBufCheckUrl.length - 1
          url = arrBufCheckUrl.get(index)
          arrBufCheckUrl.trimEnd(1)
          val arrBufNewPair = LoadHtmlItemFromPage(url)
          val newArticles = arrBufNewPair._1
          //val newGetedUrls = arrBufNewPair._2

          //val newUrls = newGetedUrls.filter(elem => ! containsInArray(arrBufOldUrl, elem))
          //arrBufCheckUrl ++= newGetedUrls
          arrBufArticle ++= newArticles
        }
      catch {
         case _  => println("Ошибка")
      }
    }

    arrBufOldUrl.foreach(elem => println(elem))
    arrBufArticle.toArray
  }

}

object HtmlParser{
  val wordsForHtmlItems = ReaderRules.getWordsForHtmlItems
  val WordsForInvalidUrl = ReaderRules.getWordsForInvalidUrl
  val WordsForCheckUrl = ReaderRules.getWordsForCheckUrl
}