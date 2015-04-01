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
  val parser = new HtmlParser("http://habrahabr.ru/")
  val array = parser.getAllArticle("http://habrahabr.ru/")
    println(array.size)
    ArticleWriter.WriteToFiles(array)


}

}
