package JsoupTest

import org.jsoup.Jsoup
import org.jsoup.select.Elements
import org.jsoup.nodes.Element
import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import org.jsoup.select.Elements

import scala.collection.JavaConversions._
import scala.collection.mutable._

/**
 * Created by Александр on 15.03.2015.
 */
object GetHtmlItems {
  def main(args: Array[String]): Unit = {
   val a : getterArticle.HtmlItem = getterArticle.HtmlItem.CreateHtmlItem("http://habrahabr.ru/post/225653/")
    println(a)

//    for (element <- elementsWithClass ) {
//      println(element.text())
//    }
//    println(elementsWithClass.get(0).text())
//    print(doc.title())
//    htmlItems.foreach(item => println(item.toString+'\n'))
//    System.out.println(htmlItems.length)
  }
}
