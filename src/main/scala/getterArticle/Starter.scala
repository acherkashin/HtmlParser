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

      val readerConfigurations = new ReaderConfigurations("configuration.json")     //считываем правила по указанному имени
      val countSites = readerConfigurations.CountSites                              // получаем количество сайтов

      val bufferArticle = new ArrayBuffer[HtmlItem]()

      for (i <- 0 until countSites ) {
        val countPages = readerConfigurations.getCountPages
        for( j <- 0 until countPages){
          val parser = new HtmlParser(readerConfigurations)                           // передаём в конструктор читателя конфига
          val array = parser.LoadHtmlItemFromPage()                                   // считываем статьи с указанного сайта
          logger.write("Количество статей: "+array.size.toString)                     // записываем количество статей в конфиг

          bufferArticle ++= array
          readerConfigurations.nextPage
        }
        readerConfigurations.nextSite                                             // устанавливаем следующий сайт
      }

      val articleWriter = new ArticleWriter()
      articleWriter.WriteToFiles(bufferArticle.toArray)

    }catch{
      case e: Exception => logger.write(e.getMessage)
    }

}

}
