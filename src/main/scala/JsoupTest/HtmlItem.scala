package JsoupTest
import org.apache.commons.lang3.StringEscapeUtils

/**
 * Created by Александр on 15.03.2015.
 */
class HtmlItem (val url: String, val description: String) {
  def ToString(): String = s"url: $url\ndescription:$description\n"

  def toJson():String = {
    val sb = new StringBuilder
    sb.append("{\n")
    sb.append("  \"url\": \"" + StringEscapeUtils.escapeJson(url) + "\",\n")
    sb.append("  \"snippet\": \"" + StringEscapeUtils.escapeJson(description) + "\",\n")
    sb.append("}")

    sb.toString()
  }
}


