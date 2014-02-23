package models

import play.api.mvc.Controller
import play.modules.reactivemongo.MongoController
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.{BSONString, BSONInteger, BSONDocument}
import java.util.concurrent.TimeUnit
import scala.concurrent.{Future, Await}
import scala.concurrent.duration.Duration
import play.api.libs.concurrent.Execution.Implicits._

/**
 * Created by bdickele
 * Date: 2/22/14
 */
object MenuRW extends Controller with MongoController {

  def collectionCategory = db.collection[BSONCollection]("category")
  def collectionGallery = db.collection[BSONCollection]("gallery")

  val findAllQuery = BSONDocument("online" -> true)


  def createMenu(): Future[List[MenuItemCategory]] = {

    def getMenuGalleries(categoryId: Int): Option[List[MenuItemGallery]] = {

      def accLeaves(docs: List[BSONDocument], acc: List[MenuItemGallery]): List[MenuItemGallery] = docs match {
        case Nil => acc
        case doc :: tail => accLeaves(tail,
          MenuItemGallery(doc.getAs[BSONString]("title").get.value, doc.getAs[BSONInteger]("galleryId").get.value) :: acc)
      }

      val future: Future[List[BSONDocument]] = collectionGallery.
        find(BSONDocument("online" -> true, "categoryId" -> categoryId)).
        sort(BSONDocument("rank" -> 1)).
        cursor[BSONDocument].
        collect[List]()

      val galleryDocs = Await.result(future, Duration(5, TimeUnit.SECONDS))
      if (galleryDocs.size == 0) None
      else Some(accLeaves(galleryDocs, List()))
    }

    def createMenuCategories(listCategoryDoc: List[BSONDocument], acc: List[MenuItemCategory]): List[MenuItemCategory] =
      listCategoryDoc match {
        case Nil => acc
        case doc :: tail => createMenuCategories(tail,
          MenuItemCategory(doc.getAs[BSONString]("title").get.value,
            getMenuGalleries(doc.getAs[BSONInteger]("categoryId").get.value)) :: acc)
      }

    val future = collectionCategory.
      find(findAllQuery).
      sort(BSONDocument("rank" -> 1)).
      cursor[BSONDocument].
      collect[List]()

    future.map(docs => createMenuCategories(docs, List()))
  }

}
