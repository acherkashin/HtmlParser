import java.io.{PrintWriter, StringWriter}

package object common {
     def using[T <: { def close() }] (resource: T) (func: T => Unit) =
         try { func(resource) } finally { if (resource != null) resource.close() }


     def usage[A, B <: {def close(): Unit}] (closeable: B) (f: B => A): A =
       try { f(closeable) } finally { closeable.close() }


     def loadStringFromFile(path: String): String = {
         common.usage(scala.io.Source.fromFile(path, "UTF-8")) {
             file => file.mkString
           }
       }

}
