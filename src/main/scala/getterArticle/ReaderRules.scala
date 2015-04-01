package getterArticle

import java.nio.file.Paths
import com.google.gson._
import scala.io.Source
/**
 * Created by Александр on 29.03.2015.
 */
object ReaderRules{
  private val fileName   = "Rules.json"
  private val homeDir    = System.getProperty("user.dir")
  private val pathToFile = Paths.get(homeDir, fileName).toString

  private def getWordsByProperty(property : String): Array[String]={
     val source  = Source.fromFile(pathToFile)
     val strJson = source.mkString

     val parser: JsonParser = new JsonParser
     val obj: JsonElement   = parser.parse(strJson)
     val jsonObj = obj.asInstanceOf[JsonObject]
     val jsonArrayRequiredWords = jsonObj.get(property).getAsJsonArray

     val size = jsonArrayRequiredWords.size()
     val arrayRequiredWords = for( i <- 0 until size ) yield {
                                jsonArrayRequiredWords.get(i).getAsString
                              }
     arrayRequiredWords.toArray
   }

  def getWordsForHtmlItems(): Array[String] = getWordsByProperty("htmlItems")

  def getWordsForInvalidUrl(): Array[String] = getWordsByProperty("invalidUrl")

  def getWordsForCheckUrl(): Array[String] = getWordsByProperty("checkUrl")

}
