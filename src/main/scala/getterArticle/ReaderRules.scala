package getterArticle

import java.nio.file.Paths
import com.google.gson._
import scala.io.Source
/**
 * Created by Александр on 29.03.2015.
 */
case class KeyValue(key : String, value : String)

class ReaderRules(nameFile : String){

  private val homeDir    = System.getProperty("user.dir")
  private val pathToFile =  Paths.get(homeDir, nameFile).toString
  private val jsonFile = getFileAsJson()

  private val arraySites = this.getSites
  private var currentSite = arraySites(0)
  private var jsonCurrentRules = getRulesForCurrentSite()

  private var currentIndexOfSite = 0
  private var countSites = arraySites.length

  //Общий метод для считывания правил
  private def getWordsByProperty(property : String): Array[String]={

     val jsonArrayRequiredWords = jsonCurrentRules.get(property).getAsJsonArray

     val size = jsonArrayRequiredWords.size()
     val arrayRequiredWords = for( i <- 0 until size ) yield {
                                jsonArrayRequiredWords.get(i).getAsString
                              }
     arrayRequiredWords.toArray
   }
  /*Метод берёт указанный при создании экземпляра файл
  и преобразует к объекту Json*/
  def getFileAsJson(): JsonObject = {
    val source  = Source.fromFile(pathToFile)
    val strJson = source.mkString

    val parser: JsonParser = new JsonParser
    val obj: JsonElement   = parser.parse(strJson)
    val jsonObj = obj.asInstanceOf[JsonObject]

    jsonObj
  }

  //Возвразает число сайтов
  def CountSites = { countSites }

  // метод, возвращающий правила для текущего сайта
  private def getRulesForCurrentSite(): JsonObject ={ jsonFile.get(currentSite).getAsJsonObject }

  private def hasNextSite(): Boolean ={
    var hasNext = false
    val newIndex = currentIndexOfSite + 1

    if( newIndex < countSites)
      hasNext = true

    hasNext
  }
  //метод, который устанавливает следующий сайт
  def nextSite(): Boolean = {
    var result = true

    if(hasNextSite()){
      currentIndexOfSite += 1
      currentSite = arraySites(currentIndexOfSite)
      jsonCurrentRules = getRulesForCurrentSite()
    }else
      result = false

    result
  }

  //Метод для получения массива сайтов
  def getSites : Array[String] = {
    val jsonArrayRequiredWords = jsonFile.get("sites").getAsJsonArray

    val size = jsonArrayRequiredWords.size()
    val arrayRequiredWords = for( i <- 0 until size ) yield {
      jsonArrayRequiredWords.get(i).getAsString
    }

    arrayRequiredWords.toArray
  }//получаем перечень сайтов

  //Возвращает URL текущего сайта
  def CurrentSite = { currentSite}

  //возвращает селектор и значение
  def getKeyValueArticle() : KeyValue = { val array = getWordsByProperty("content"); KeyValue(array(0), array(1)) }

  def getKeyValueDateTime() : KeyValue = { val array = getWordsByProperty("dateTime"); KeyValue(array(0), array(1)) }

  def getWordsForHtmlItems(): Array[String] = getWordsByProperty("htmlItems")

  def getWordsForInvalidUrl(): Array[String] = getWordsByProperty("invalidUrl")

  def getWordsForCheckUrl(): Array[String] = getWordsByProperty("prefCheckUrl")

}
