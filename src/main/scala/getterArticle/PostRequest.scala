package getterArticle

import java.io.{BufferedReader, InputStreamReader}
import java.net.URL
import sun.net.www.protocol.http.HttpURLConnection

abstract class RequestResult
case class DocExists(url: String) extends RequestResult
case class OK(url: String) extends RequestResult

object PostRequest {
  def send(url: String, json: String): RequestResult = {
    val data   = json.getBytes("UTF8")                                  //получаем массив байтов
    val urlObj = new URL(url)
    val conn   = urlObj.openConnection.asInstanceOf[HttpURLConnection]  //получаем экземпляр класса "HttpURLConnection"

    try {
      init(conn)                                  //инициализация запроса
      writeRequest(conn, data)
      val resp = getResponse(conn)                //получаем ответ
      resp match {
        case "exists" => DocExists(url)
        case "ok"     => OK(url)
        case x        => throw new Exception(x)
      }
    } finally {
      conn.disconnect
    }
  }

  private def init(conn: HttpURLConnection): Unit = {
    conn.setDoInput(true)                                        //соединение может использоваться для ввода
    conn.setDoOutput(true)                                       //соединение может использоваться для вывода
    conn.setUseCaches(false)                                     //протоколу позволяется делать кэширование
    conn.setInstanceFollowRedirects(false)                       //?????????????????
    conn.addRequestProperty("Content-Type", "application/json")  //устанавливаем свойства запроса
    conn.setRequestMethod("POST")                                //будет использоваться метод POST
  }

  private def writeRequest(conn: HttpURLConnection, data: Array[Byte]): Unit = {
    conn.setRequestProperty("Content-Length", data.length.toString)    //Устанавливает общее свойство запроса
    common.using(conn.getOutputStream) {                               // т.е. передаём на сервер длину массива
      stream =>
        stream.write(data)
        stream.flush                                            //очищаем буферы, завершаем операцию вывода
    }
  }

  private def getResponse(conn: HttpURLConnection): String = {
    common.usage(conn.getInputStream) {                           //получаем поток ввода
      is => common.usage(new InputStreamReader(is)) {             //
        isr => common.usage(new BufferedReader(isr)) {
          br =>
            val resp = new StringBuffer
            var line = br.readLine                                //считываем строку из потока
            while (line != null) {
              if (resp.length > 0)
                resp.append('\r')                                 //для чего выравнивания текста?
              resp.append(line)
              line = br.readLine
            }

            resp.toString
        }
      }
    }
  }
}
