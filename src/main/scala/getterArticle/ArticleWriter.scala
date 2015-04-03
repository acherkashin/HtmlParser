package getterArticle

import java.io.{FileWriter, File}
import java.nio.file.Paths

/**
 * Created by Александр on 30.03.2015.
 */
class ArticleWriter(folderName : String) {
  private val recordingFolder   = "Articles"
  private val homeDir    = System.getProperty("user.dir")
  private val pathToDirectory = Paths.get(Paths.get(homeDir, folderName).toString, recordingFolder).toString


  def WriteToFiles(arrayArticle : Array[HtmlItem]): Unit = {
      val logger = new Logger("article")
      var countErrors = 0
      val directory = new File(pathToDirectory)
      if (directory.exists())
        directory.delete()//если папка существует - удаляем

        directory.mkdir()// создаём новую папку

      var name = ""
      var index = 0;
      for (htmlItem <- arrayArticle) {
        index += 1
        try {
          name = index.toString
          val fileName   = s"$name.txt"
          val pathToFile = Paths.get(pathToDirectory, fileName).toString

          val file = new File(pathToFile)
            file.createNewFile()//Создаём файл

          val wrt = new FileWriter(file)
          val text = htmlItem.toString//производим запись в файл
          wrt.append(text)

          wrt.close()
        }
        catch {
          case _ => {logger.write(s"Ошибка записи статьи $name")
                      countErrors += 1}

        }
      }
    logger.write(s"Количество ошибок записи $countErrors");
  }
}
