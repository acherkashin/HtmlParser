package getterArticle

import java.net.URL
import java.util
import java.util.regex.Pattern

import getterArticle.HtmlItem
import org.apache.commons.lang3.StringEscapeUtils

import scala.collection.mutable.ArrayBuffer
import scala.io.Source


/**
 * Created by Александр on 28.03.2015.
 */
object Starter {
  def main(args:Array[String]):Unit = {

    val logger = new Logger("article")

    try {

      val readerRules = new ReaderRules("rules.json")
      val sizeArray = readerRules.CountSites

      val bufferArticle = new ArrayBuffer[HtmlItem]()

      for (i <- 0 until sizeArray) {
        val parser = new HtmlParser(readerRules)
        val array = parser.LoadHtmlItemFromPage()
        logger.write("Количество статей: "+array.size.toString)

        bufferArticle ++= array
        readerRules.nextSite()
      }

      val articleWriter = new ArticleWriter()
      articleWriter.WriteToFiles(bufferArticle.toArray)

    }catch{
      case e: Exception => logger.write(e.getMessage)
    }

}

}
