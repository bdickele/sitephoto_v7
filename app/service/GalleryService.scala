package service

import play.api.mvc.Controller
import play.api.libs.concurrent.Execution.Implicits._
import play.modules.reactivemongo.MongoController
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import models._
import play.api.libs.json._
import models.Gallery
import play.modules.reactivemongo.json.collection.JSONCollection
import models.Category
import models.Models._

/**
 * Service for retrieving data from collection "gallery"
 * bdickele
 */
object GalleryService extends Controller with MongoController {

  def collection = db.collection[JSONCollection]("gallery")


  /**
   * @return Most recent gallery (displayed by default)
   */
  def findDefault: Future[Option[Gallery]] = {

    def findLastGalleryOfCategories(categories: List[Category]): Option[Gallery] = categories match {
      case Nil => throw new Error("That's weird but we could not find any online gallery")
      case head :: tail =>
        val futureLastGallery = findLastGalleryOfCategory(head.categoryId)
        val optionLastGallery = Await.result(futureLastGallery, 5 seconds)
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
