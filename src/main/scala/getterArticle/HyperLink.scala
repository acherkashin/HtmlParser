package getterArticle

import JsoupTest.HtmlParser
import org.apache.commons.lang3.StringEscapeUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import scala.collection.JavaConversions._

/**
 * Created by Александр on 29.03.2015.
 */

//case class CheckUrl(url: String)

case class HtmlItem(url: String,title : String, description : String) {
  override
  def toString = s"url: $url\n\ntitle: $title\n\ndescription:\n$description\n"

  def toJson = {
    val sb = new StringBuilder
    sb.append("{\n")
    sb.append("  \"url\": \"" + StringEscapeUtils.escapeJson(url) + "\",\n")
    sb.append("  \"snippet\": \"" + StringEscapeUtils.escapeJson(description) + "\n")
    sb.append("}")

    sb.toString()
  }
}

object HtmlItem{
  def CreateHtmlItem(url:String): HtmlItem = {
    val doc: Document = Jsoup.connect(url).get
    val elementsWithClass: Elements = doc.getElementsByAttributeValue("class","content html_format")
    val strbDescription = new StringBuilder

    for (element <- elementsWithClass )
      strbDescription.append(element.text())

    val title = doc.title()
    val description = strbDescription.toString

    new HtmlItem(url, title, description)
  }

}