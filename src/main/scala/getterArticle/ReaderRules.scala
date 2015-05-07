package getterArticle

import java.nio.file.Paths
import com.google.gson._
import scala.io.Source
/**
 * Created by Александр on 29.03.2015.
 */
case class KeyValue(key : String, value : String)

//case class WebSite(startPage : String,pagesOfCurrentSite : Array[String], currentPage: Int, )

class ReaderConfigurations(nameFile : String){

  private val homeDir    = System.getProperty("user.dir")               //текущая дирректория
  private val pathToFile =  Paths.get(homeDir, nameFile).toString       //путь к файлу с правилами
  private val jsonFile = getRulesFromFileAsJsonObject()                                //
 //----------------------------------Поля сайтов------------------------------------------------------------------------
  private val arraySites = this.getSites                                //массив с просматриваемыми сайтами
  private var currentSite = arraySites(0)                               //Текущий сайт
  private var jsonCurrentRules = getRulesForCurrentSite()               //правила для текущего сайта
  private var currentIndexOfSite = 0                                    //Индекс текущего сайта в массиве
  private val countSites = arraySites.length                            //Общее количество сайтов
 //-----------------------------Поля страниц----------------------------------------------------------------------------
  private var currentIndexOfPage = 0                                    //Индекс текущей страницы
  private var pagesOfCurrentSite : Array[String] = this.getPagesOfCurrentSites //Массив страниц
  private var currentPage: String = this.getDefaultPage                 //Текущая страница
  private var countPages  = this.getCountPages                          // Количество страниц

  //----------------------------------------------Функции для работы со страницами-------------------------------------
  def getPagesOfCurrentSites()={
    val jsArrPages = getRulesForCurrentSite().get("pages").getAsJsonArray

    val size = jsArrPages.size()
    val arrayRequiredWords = for( i <- 0 until size ) yield {
      jsArrPages.get(i).getAsString
    }

    arrayRequiredWords.toArray
  }

  private def hasNextPage(): Boolean ={
    var hasNext = false
    val newIndex = currentIndexOfPage + 1

    if( newIndex < countPages)
      hasNext = true

    hasNext
  }

  def nextPage(): Boolean = {
    var result = true

    if(this.hasNextPage()){
      currentIndexOfPage += 1
      currentPage = pagesOfCurrentSite(currentIndexOfPage)
    }else
      result = false

    result
  }

  def setCountPages(count : Int)  = { this.countPages = count }

  def getCountPages() = { pagesOfCurrentSite.length }

  def getDefaultPage()  = {  this.pagesOfCurrentSite(0) }  //Страница по умолчанию, нулевой элемент массива "pagesOfCurrentSite"

  //def setCurrentPage(page : String) ={ this.currentPage = page }

  def getCurrentPage() = { currentPage }
  //-------------------------------------------------------------------------------------------------------------------

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
  def getRulesFromFileAsJsonObject(): JsonObject = {
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
    if(this.hasNextSite()){
      currentIndexOfSite += 1
      currentSite = arraySites(currentIndexOfSite)
      jsonCurrentRules = getRulesForCurrentSite()

      currentIndexOfPage = 0                                    //сбрасываем текущий индекс
      pagesOfCurrentSite = this.getPagesOfCurrentSites          //загружаем перечень страниц для установленного сайта
      countPages = this.getCountPages                           //получаем количество страниц
      currentPage = this.getDefaultPage                         //устанавливаем страницу по умолчанию
    }else
      result = false

    result
  }

  //Метод для получения массива сайтов
  def getSites : Array[String] = {
    val jsArrSites = jsonFile.get("sites").getAsJsonArray

    val size = jsArrSites.size()
    val arrayRequiredWords = for( i <- 0 until size ) yield {
      jsArrSites.get(i).getAsString
    }

    arrayRequiredWords.toArray
  }

  //Возвращает URL текущего сайта
  def CurrentSite = { currentSite}

  //возвращает селектор и значение
  def getKeyValueArticle() : KeyValue = { val array = getWordsByProperty("content"); KeyValue(array(0), array(1)) }

  def getKeyValueDateTime() : KeyValue = { val array = getWordsByProperty("dateTime"); KeyValue(array(0), array(1)) }

  def getWordsForHtmlItems(): Array[String] = getWordsByProperty("htmlItems")

  def getWordsForInvalidUrl(): Array[String] = getWordsByProperty("invalidUrl")

  def getWordsForCheckUrl(): Array[String] = getWordsByProperty("prefCheckUrl")

  def getPages(): Array[String] = getWordsByProperty("pages")

}
