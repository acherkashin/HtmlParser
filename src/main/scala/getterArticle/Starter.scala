package getterArticle

import java.util

import getterArticle.HtmlItem
import org.apache.commons.lang3.StringEscapeUtils

import scala.collection.mutable.ArrayBuffer


/**
 * Created by Александр on 28.03.2015.
 */
object Starter {
  def main(args:Array[String]):Unit = {
//    ReaderRules.getWordsForInvalidUrl().foreach(i => println(i))
    //    ReaderRules.getWordsForCheckUrl.foreach(i => println(i))
    try{
      val parser1 = new HtmlParser("HabraHabr")
      val array1 = parser1.LoadHtmlItemFromPage()
        println(array1.size)
        val articleWriter = new ArticleWriter("HabraHabr")
          articleWriter.WriteToFiles(array1)

        val parser2 = new HtmlParser("linux.org")
        val array2 = parser2.LoadHtmlItemFromPage()
        println(array2.size)
        val articleWriter2 = new ArticleWriter("linux.org")
        articleWriter2.WriteToFiles(array2)
    }
    catch {
      case _  => println("Ошибка")
    }

}

}
