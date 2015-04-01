package getterArticle

import java.io.{FileWriter, File}
import java.nio.file.Paths

/**
 * Created by Александр on 30.03.2015.
 */
object ArticleWriter {
  private val folderName   = "Articles"
  private val homeDir    = System.getProperty("user.dir")
  private val pathToDirectory = Paths.get(homeDir, folderName).toString

  def WriteToFiles(arrayArticle : Array[HtmlItem]): Unit = {
      var countErrors = 0;
      val directory = new File(pathToDirectory)
      if (!directory.exists())
        directory.mkdir()

      for (htmlItem <- arrayArticle) {

          val name = htmlItem.title.substring(0, 15)
          val fileName = s"$name.txt"
          val pathToFile = Paths.get(pathToDirectory, fileName).toString
        try {
          val file = new File(pathToFile)
          if (!file.exists())
            file.createNewFile()

          val wrt = new FileWriter(file)

          val text = htmlItem.toString
          wrt.append(text)
          wrt.close()
        }
        catch {
          case _ => {println(s"Ошибка записи статьи $name")
                      countErrors += 1}

        }
      }
  }
}
