package JsoupTest

/**
 * Created by Александр on 15.03.2015.
 */
object GetHtmlItems {
  def main(args: Array[String]): Unit = {
    val htmlParser = new HtmlParser("http://habrahabr.ru/")
    val htmlItems = htmlParser.LoadHtmlItem()

    htmlItems.foreach(item => println(item.toString+'\n'))
    System.out.println(htmlItems.length)
  }
}
