package getterArticle


import org.apache.commons.lang3.StringEscapeUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import scala.collection.JavaConversions._

/**
 * Created by Александр on 29.03.2015.
 */

//case class CheckUrl(url: String)

case class HtmlItem(url: String,title : String, description : String, dateTime : String) {
  override
  def toString = s"url: $url\n\ntitle: $title\n\ndate: $dateTime\n\ndescription:\n$description\n"

  def toJson = {
    val sb = new StringBuilder
    sb.append("{\n")
    sb.append("  \"url\": \"" + StringEscapeUtils.escapeJson(url) + "\",\n")
    sb.append("  \"title\": \"" + StringEscapeUtils.escapeJson(title) + "\",\n")
    sb.append("  \"date\": \"" + StringEscapeUtils.escapeJson(dateTime) + "\",\n")
    sb.append("  \"snippet\": \"" + StringEscapeUtils.escapeJson(description) + "\n")
    sb.append("}")

    sb.toString()
  }
}

object HtmlItem{
  //меод получает 1)url и 2)селектор со значением, по которому он будет
  //получать содержимое статьи
  def CreateHtmlItem(url:String, keyValueArticle: KeyValue, keyValueDateTime: KeyValue): HtmlItem = {
    val doc: Document = Jsoup.connect(url).get

    val elementsWithClass: Elements = doc.getElementsByAttributeValue(keyValueArticle.key, keyValueArticle.value)

    val title = doc.title
    val description = elementsWithClass.text
    val dateTime = doc.getElementsByAttributeValue(keyValueDateTime.key, keyValueDateTime.value).text

    new HtmlItem(url, title, description, dateTime)
  }

}