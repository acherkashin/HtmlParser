package getterArticle

import java.util

/**
 * Created by Александр on 28.03.2015.
 */
object Starter {
  val postUrl = "http://188.226.178.169:3000/insert-document"
  val logger = new Logger("article")

  def main(args:Array[String]):Unit = {
    try {
      val readerConfigurations = new ReaderConfigurations("configuration.json")     //считываем правила по указанному имени
      val countSites = readerConfigurations.CountSites                              // получаем количество сайтов

      val bufferArticle = new util.HashSet[HtmlItem]()

      for (i <- 0 until countSites ) {
        val countPages = readerConfigurations.getCountPages

        for( j <- 0 until countPages){
          val parser = new HtmlParser(readerConfigurations)                           // передаём в конструктор читателя конфига
          val array = parser.LoadHtmlItemFromPage()                                   // считываем статьи с указанного сайта
          logger.write("Количество статей: "+array.size.toString)                     // записываем количество статей в лог Файл
          //AddArrayToSet(array, bufferArticle)                                       // записываем массив статей в множество

          array.foreach( postItem( _ ) )                                              // отправляем данные на сервер
          readerConfigurations.nextPage                                               // переходим на следующую страницу
        }
        readerConfigurations.nextSite                                                 // переходим на следующий сайт
      }

      val articleWriter = new ArticleWriter()
      val array = new Array[HtmlItem](bufferArticle.size)
      bufferArticle.toArray(array)

      articleWriter.WriteToFiles(array)

    }catch{
      case e: Exception => logger.write(e.getMessage)
    }

}
  def AddArrayToSet(array : Array[HtmlItem],hashSet : util.HashSet[HtmlItem]): Unit ={
    for(item <- array)
    {
      hashSet.add(item)
    }
  }

  private def postItem(item: HtmlItem) {
    try{

      val res = PostRequest.send(postUrl, item.toJson)
      res match {
        case x: DocExists => logger.write("DocExists: "+item.url)
        case x: OK        => logger.write("OK"+item.url)
      }
    }catch {
      case ex: Exception => logger.write("Error: "+ex.getMessage)
    }
  }


}

