package com.mlh.spraysample
package basic

import org.json4s._
import spray.httpx.Json4sSupport
import spray.routing._

/* Used to mix in Spray's Marshalling Support with json4s */
object Json4sProtocol extends Json4sSupport {
  implicit def json4sFormats: Formats = DefaultFormats
}

/* Our case class, used for request and responses */
case class Foo(bar: String)

/* Our route directives, the heart of the service.
 * Note you can mix-in dependencies should you so chose */
trait SpraySampleService extends HttpService {
  import Json4sProtocol._

  val spraysampleRoute = {
    path("entity") {
        get {
          complete(List(Foo("foo1"), Foo("foo2")))
        }
      }
  }
}
