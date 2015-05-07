package getterArticle

import java.util

import org.joda.time.DateTime

/**
 * Created by Александр on 28.03.2015.
 */
object Starter {
  private val postUrl = "http://188.226.178.169:3000/insert-document"
  private val logger = new Logger("article")
  private val reloadDelaySec = 30*60
  private val nameConfigFile = "configuration.json"

  def main(args:Array[String]):Unit = {
    try {
      infiniteLoop                                            //val articleWriter = new ArticleWriter //articleWriter.WriteToFiles(array)
    }catch{
      case e: Exception => logger.write(e.getMessage)
    }
 }

  private def AddArrayToSet(array : Array[HtmlItem],hashSet : util.HashSet[HtmlItem]): Unit ={
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
        case x: OK        => logger.write("OK: "+item.url)
      }
    }catch {
      case ex: Exception => logger.write("Error: "+ex.getMessage)
    }
  }

  private def getAllHtmlItems(): util.HashSet[HtmlItem] ={
    val readerConfigurations = new ReaderConfigurations(nameConfigFile)           //считываем правила по указанному имени файла
    val countSites = readerConfigurations.CountSites                              // получаем количество сайтов
    val setArticle = new util.HashSet[HtmlItem]()

    for (i <- 0 until countSites ) {
      try {
        val countPages = readerConfigurations.getCountPages

        for (j <- 0 until countPages) {
          try {
            val parser = new HtmlParser(readerConfigurations)                     // передаём в конструктор читателя конфига
            val array = parser.LoadHtmlItemFromPage()                             // считываем статьи с указанного сайта
            logger.write("Количество статей: " + array.size.toString)             // записываем количество статей в лог Файл
            AddArrayToSet(array, setArticle)                                      // записываем массив статей в множество

            readerConfigurations.nextPage                                         // переходим на следующую страницу
          } catch {
            case ex: Exception => logger.write(ex.getMessage)
          }
        }
      }catch{
        case ex: Exception => logger.write(ex.getMessage)
      }
      readerConfigurations.nextSite
    }

    setArticle
  }

  private def infiniteLoop(): Unit = {
    var lastLoad: Option[DateTime] = None
    while(true){
      try{
        if (shouldLoad(lastLoad)){
          val setArticle = this.getAllHtmlItems
          val arrayArticle = HashSetToArray(setArticle)

          arrayArticle.foreach( postItem( _ ) )                                              // отправляем данные на сервер
          lastLoad = Some(DateTime.now)
        }
       // Thread.sleep(10000)
      }
      catch {
        case x :Exception  => logger.write("Ошибка: " + x.getMessage)
      }
    }
  }

  private def shouldLoad(lastLoad: Option[DateTime]) : Boolean= {
    lastLoad match {
               case None    => true
               case Some(x) => DateTime.now.isAfter(x.plusSeconds(reloadDelaySec))
            }
  }

  private def HashSetToArray(hashSet : util.HashSet[HtmlItem]): Array[HtmlItem] ={
    val array = new Array[HtmlItem](hashSet.size)
    hashSet.toArray(array)

    array
  }


}

