package app.models

import org.specs2.mutable.Specification
import scala.concurrent.Await
import scala.concurrent.duration._
import play.api.libs.json.{JsArray, JsObject}
import service.MenuService
import app.TestApplication

/**
 * Created by bdickele
 * Date: 2/23/14
 */
class MenuServiceSpec extends Specification {

  "Method menu" should {

    lazy val future = MenuService.createMenu()
    lazy val categories: List[JsObject] = Await.result(future, Duration(5, SECONDS))

    "return complete menu" in new TestApplication {

      categories.length must equalTo(10)

      val firstCategory = categories.head
      println(firstCategory)
      (firstCategory \ "label").as[String] must equalTo("2013")

      val lastCategory = categories.last
      (lastCategory \ "label").as[String] must equalTo("2004")

      val galleries = (lastCategory \ "children").as[List[JsObject]]
      galleries.size must equalTo(2)

      val gallery = galleries.head
      (gallery \ "label").as[String] must equalTo("Août 2004 : Mantoue et Venise")
      (gallery \ "id").as[Int] must equalTo(2)
    }
  }
}
