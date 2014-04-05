package service

import play.api.mvc.Controller
import play.api.libs.concurrent.Execution.Implicits._
import play.modules.reactivemongo.MongoController
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.libs.json._
import play.api.libs.json.Reads._

/**
 * Service returning JSon data used to build menu.
 * That service returns data JsObject.
 * Created by bdickele
 */
object MenuService extends Controller with MongoController {

  def collectionCategory = db.collection[JSONCollection]("category")

  def collectionGallery = db.collection[JSONCollection]("gallery")


  def createMenu(): Future[List[JsObject]] = {

    def createMenuGalleries(categoryId: Int): List[JsObject] = {

      def accLeaves(docs: List[JsObject], acc: List[JsObject]): List[JsObject] = docs match {
        case Nil => acc
        case head :: tail => accLeaves(tail,
          Json.obj("id" -> (head \ "galleryId").as[Int], "label" -> (head \ "title").as[String]) :: acc)
      }

      val future = collectionGallery.
        find(Json.obj("online" -> true, "categoryId" -> categoryId)).
        sort(Json.obj("rank" -> 1)).
        cursor[JsObject].
        collect[List]()

      val galleryObjects = Await.result(future, 5 seconds)
      accLeaves(galleryObjects, List())
    }

    def createMenuCategories(categoryObjects: List[JsObject], acc: List[JsObject]): List[JsObject] = categoryObjects match {
      case Nil => acc
      case head :: tail =>
        val categoryTitle = (head \ "title").as[String]
        val categoryId = (head \ "categoryId").as[Int]
        createMenuCategories(tail,
          Json.obj("label" -> categoryTitle, "children" -> createMenuGalleries(categoryId)) :: acc)
    }

    val future = collectionCategory.
      find(Json.obj("online" -> true)).
      sort(Json.obj("rank" -> 1)).
      cursor[JsObject].
      collect[List]()

    future.map(docs => createMenuCategories(docs, List()))
  }

}
