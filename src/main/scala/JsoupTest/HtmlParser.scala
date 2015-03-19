package JsoupTest

import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import org.jsoup.select.Elements

import scala.collection.JavaConversions._
import scala.collection.mutable._

/**
 * Created by Александр on 15.03.2015.
 */
class HtmlParser(strUrl: String){
  private val invalidEnds    = Array(".jpg",".css",".jpg",".png")
  private val requiredPrefix = Array("http://","https://")
  private val invalidWords   = Array("/user","/register","/auth","/login","#")

  private def hasInvalidEnd(hyperLink: String): Boolean ={
    var hasInvEnd = false

    for(end <- invalidEnds if(hasInvEnd == false) ) {
      if(hyperLink.endsWith(end) == true)
        hasInvEnd = true
    }
    hasInvEnd
  }

  private def hasRequiredPrefix(hyperLink: String): Boolean ={
    var hasReqPrefix = false

    for(prefix <- requiredPrefix if(hasReqPrefix == false) ) {
      if(hyperLink.startsWith(prefix) == true)
        hasReqPrefix = true
    }
    hasReqPrefix
  }

  private def hasInvalidWord(hyperLink: String): Boolean ={
    var hasInvWord = false

    for (word <- invalidWords if(hasInvWord == false)){
      if(hyperLink.indexOf(word) > 0)
        hasInvWord = true
    }

    hasInvWord
  }

  private def IsValid(element: Element): Boolean ={
    var result = false
    val hyperLink = element.attr("href")

    val contentIsEmpty = element.text().isEmpty
    if(! contentIsEmpty) {
      val hasInvalEnd = hasInvalidEnd(hyperLink)
      if (! hasInvalEnd) {
        val hasReqPrefix = hasRequiredPrefix(hyperLink)
        if (hasReqPrefix) {
          val hasInvalWord = hasInvalidWord(hyperLink)
          if (! hasInvalWord)
            result = true
        }
      }
    }

    result
  }

  def LoadHtmlItem(): Array[HtmlItem] ={

    val doc: Document = Jsoup.connect(strUrl).get
    val elementsWithAttr: Elements = doc.getElementsByAttribute("href")
    val htmlItems: HashSet[HtmlItem] = new HashSet[HtmlItem]()

    for (element <- elementsWithAttr if (IsValid(element) == true)) {
      val item = HtmlItem(element.attr("href"), element.text())
      htmlItems.add(item)
    }

    htmlItems.toArray
  }
}
