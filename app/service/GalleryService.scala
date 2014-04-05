package service

import play.api.mvc.Controller
import play.api.libs.concurrent.Execution.Implicits._
import play.modules.reactivemongo.MongoController
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import models._
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import models.Gallery
import models.GalleryPic
import play.modules.reactivemongo.json.collection.JSONCollection
import models.Category

/**
 * Service for retrieving data from collection "gallery"
 * bdickele
 */
object GalleryService extends Controller with MongoController {

  def collection = db.collection[JSONCollection]("gallery")


  // Mapper from JsObject to GalleryBasic
  implicit val gallerySimpleReader: Reads[GalleryBasic] = (
    (__ \ "categoryId").read[Int] and
      (__ \ "galleryId").read[Int] and
      (__ \ "rank").read[Int])(GalleryBasic.apply _)

  // Mappers to convert a JsObject into a Gallery

  implicit val galleryPicReader: Reads[GalleryPic] = (
    (__ \ "thumbnail").read[String] and
      (__ \ "web").read[String] and
      (__ \ "comment").readNullable[String]
    )(GalleryPic.apply _)

  implicit val galleryReader: Reads[Gallery] = (
    (__ \ "categoryId").read[Int] and
      (__ \ "galleryId").read[Int] and
      (__ \ "title").read[String] and
      (__ \ "comment").readNullable[String] and
      (__ \ "pictures").read[List[GalleryPic]]
    )(Gallery.apply _)


  /**
   * @return Most recent gallery (displayed by default)
   */
  def findDefault: Future[Option[Gallery]] = {

    def findLastGalleryOfCategories(categories: List[Category]): Option[Gallery] = categories match {
      case Nil => throw new Error("That's weird but we could not find any online gallery")
      case head :: tail =>
        val futureLastGallery = findLastGalleryOfCategory(head.categoryId)
        println("-> 1")
        val optionLastGallery = Await.result(futureLastGallery, 5 seconds)
        println("-> 2")
        optionLastGallery match {
          case None => findLastGalleryOfCategories(tail)
          case some => some
        }
    }

    CategoryService.findAll.map(findLastGalleryOfCategories)
  }

  def find(galleryId: Int): Future[Option[Gallery]] =
    collection.
      find(Json.obj("galleryId" -> galleryId, "online" -> true)).
      one[Gallery]


  def findBasic(galleryId: Int): Future[Option[GalleryBasic]] =
    collection.
      find(Json.obj("galleryId" -> galleryId, "online" -> true)).
      one[GalleryBasic]

  /**
   * Purpose of that method is to returning gallery, of the same category, that is right before a
   * gallery with passed rank. If currentRank is 0 then it will return None
   * @param categoryId Category ID
   * @param currentRank Index of the gallery in the category
   * @return
   */
  def findPreviousGalleryInCategory(categoryId: Int, currentRank: Int): Future[Option[GalleryBasic]] =
    collection.
      find(Json.obj("categoryId" -> categoryId, "online" -> true, "rank" -> Json.obj("$lt" -> currentRank))).
      sort(Json.obj("rank" -> -1)).
      one[GalleryBasic]

  /**
   * Purpose of that method is to returning gallery, of the same category, that is right after a
   * gallery with passed rank. If currentRank has a rank higher than all its siblings, then it will return None
   * @param categoryId Category ID
   * @param currentRank Index of the gallery in the category
   * @return
   */
  def findNextGalleryInCategory(categoryId: Int, currentRank: Int): Future[Option[GalleryBasic]] =
    collection.
      find(Json.obj("categoryId" -> categoryId, "online" -> true, "rank" -> Json.obj("$gt" -> currentRank))).
      sort(Json.obj("rank" -> 1)).
      one[GalleryBasic]

  def findLastGalleryBasicOfCategory(categoryId: Int): Future[Option[GalleryBasic]] =
    collection.
      find(Json.obj("categoryId" -> categoryId, "online" -> true)).
      sort(Json.obj("rank" -> -1)).
      one[GalleryBasic]

  def findLastGalleryOfCategory(categoryId: Int): Future[Option[Gallery]] =
    collection.
      find(Json.obj("categoryId" -> categoryId, "online" -> true)).
      sort(Json.obj("rank" -> -1)).
      one[Gallery]

  def findFirstGalleryBasicOfCategory(categoryId: Int): Future[Option[GalleryBasic]] =
    collection.
      find(Json.obj("categoryId" -> categoryId, "online" -> true)).
      sort(Json.obj("rank" -> 1)).
      one[GalleryBasic]
}
