package WorkWithHtml

import java.nio.file.Paths

import com.google.gson.{JsonElement, JsonObject, JsonParser}

import scala.io.Source

case class KeyAndValue(Key: String, Value: String)

class Configuration(kavDateTime: KeyAndValue,
                    kavContent: KeyAndValue,
                    invalidParts: Array[String],
                    oneOfRequiredParts: Array[String]) {
  def getKeyAndValueDataTime(): KeyAndValue = kavDateTime
  def getKeyAndValueContent(): KeyAndValue = kavContent
  def getinvalidParts(): Array[String] = invalidParts
  def getTemplateURL(): Array[String] = oneOfRequiredParts

}



/**
 * Created by Александр on 29.08.2015.
 */
class ReaderConfiguration(nameFile : String){

  private val homeDir    = System.getProperty("user.dir")               //текущая дирректория
  private val pathToFile =  Paths.get(homeDir, nameFile).toString       //путь к файлу с правилами
  private val jsonConfigFile = getConfigFileAsJsonObject()                                //

  //Общий метод для считывания правил
  private def getElementByProperty(jsonConfig: JsonObject,property : String): Array[String]={

    val jsonArrayRequiredWords = jsonConfig.get(property).getAsJsonArray

    val size = jsonArrayRequiredWords.size()
    val arrayRequiredWords = for( i <- 0 until size ) yield {
      jsonArrayRequiredWords.get(i).getAsString
    }
    arrayRequiredWords.toArray
  }

  /*Метод берёт указанный при создании экземпляра файл
  и преобразует к объекту Json*/
  private def getConfigFileAsJsonObject(): JsonObject = {
    val strJson = Source.fromFile(pathToFile).mkString  //считываем содержимое конфигурационного файла

    val obj: JsonElement   = new JsonParser().parse(strJson)
    val jsonObj = obj.asInstanceOf[JsonObject]

    jsonObj
  }


  //Метод для получения массива сайтов
  private def getSiteNames : Array[String] = {
    val jsArrSites = jsonConfigFile.get("sites").getAsJsonArray

    val arraySites = for( i <- 0 until jsArrSites.size() ) yield {
      jsArrSites.get(i).getAsString
    }
    arraySites.toArray
  }


  private def getKeyAndValueContent(jsonConfig: JsonObject) : KeyAndValue = { val array = getElementByProperty(jsonConfig, "content"); KeyAndValue(array(0), array(1)) }

  private def getKeyAndValueDateTime(jsonConfig: JsonObject) : KeyAndValue = { val array = getElementByProperty(jsonConfig, "dateTime"); KeyAndValue(array(0), array(1)) }

  private def getInvalidPartsOfUrl(jsonConfig: JsonObject): Array[String] = getElementByProperty(jsonConfig, "invalidPartsURL")

  private def getTemplateURL(jsonConfig: JsonObject): Array[String] = getElementByProperty(jsonConfig,"templateURL")

  private def getPages(jsonConfig: JsonObject): Array[String] = getElementByProperty(jsonConfig, "pages")

  private def getJSONConfigForSite(siteName: String): JsonObject ={ jsonConfigFile.get(siteName).getAsJsonObject }

  private def createConfig(siteName : String ): Configuration =
  {
    val jsonConfig = getJSONConfigForSite(siteName)
    val kavDateTime = getKeyAndValueDateTime(jsonConfig)
    val kavContent = getKeyAndValueContent(jsonConfig)
    val invalidPartsOfUrl = getInvalidPartsOfUrl(jsonConfig)
    val templateURL = getTemplateURL(jsonConfig)

    new Configuration(kavDateTime, kavContent,invalidPartsOfUrl, templateURL)
  }

  private def createWebSite(siteName: String): WebSite = new WebSite(getPages( getJSONConfigForSite(siteName) ), createConfig(siteName) )

  def getWebSites(): Array[WebSite] = {
    for ( nameSite <- getSiteNames ) yield { createWebSite(nameSite) }
  }
}
