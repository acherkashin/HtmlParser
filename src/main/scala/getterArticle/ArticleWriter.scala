package getterArticle

import java.io.{FileWriter, File}
import java.nio.file.Paths

/**
 * Created by Александр on 30.03.2015.
 */
class ArticleWriter() {
  private val recordingFolder   = "Articles"
  private val homeDir    = System.getProperty("user.dir")
  private val pathToDirectory = Paths.get(homeDir, recordingFolder).toString

  private def workWithDirectory(): Unit ={
    val directory = new File(pathToDirectory)
    if (directory.exists()) {
      directory.delete() //если папка существует - удаляем
      Thread.sleep(5000);
    }

    directory.mkdir()// создаём новую папку
  }

  private def writeToFile(name : String, htmlItem: HtmlItem): Unit ={
    val fileName   = s"$name.txt"
    val pathToFile = Paths.get(pathToDirectory, fileName).toString

    val file = new File(pathToFile)
    file.createNewFile()                    //Создаём файл
    val wrt = new FileWriter(file)
    val text = htmlItem.toString
    wrt.append(text)                        //Записываем в файл

    wrt.close()
  }

  def WriteToFiles(arrayArticle : Array[HtmlItem]): Unit = {
      val logger = new Logger("article")
      var countErrors = 0
      workWithDirectory()

      var name = ""
      var index = 0;
      for (htmlItem <- arrayArticle) {
        index += 1

        try {
          name = index.toString
          writeToFile(name, htmlItem)
        }
        catch {
          case _ => {logger.write(s"Ошибка записи статьи $name")
                      countErrors += 1}

        }
      }
    logger.write(s"Количество ошибок записи $countErrors");
  }
}
