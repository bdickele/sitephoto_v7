package models

import play.api.mvc.Controller
import play.modules.reactivemongo.MongoController
import reactivemongo.api.collections.default.BSONCollection
import scala.concurrent.{Await, Future}
import reactivemongo.bson.BSONDocument
import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit

/**
 * Created by bdickele
 * Date: 2/18/14
 */
object GalleryRW extends Controller with MongoController {

  def collection = db.collection[BSONCollection]("gallery")

  /**
   * @return Most recent gallery (displayed by default)
   */
  def findDefault: Future[Option[Gallery]] = {

    def findLastGalleryOfCategories(categories: List[CategorySimple]): Option[Gallery] = categories match {
      case Nil => throw new Error("That's weird but we could not find any online gallery")
      case head :: tail =>
        val futureLastGallery = findLastGalleryOfCategory(head.categoryId)
        val optionLastGallery = Await.result(futureLastGallery, Duration(5, TimeUnit.SECONDS))
        optionLastGallery match {
          case None => findLastGalleryOfCategories(tail)
          case some => some
        }
    }

    CategoryRW.findAll.map(findLastGalleryOfCategories)
  }

  def find(galleryId: Int): Future[Option[Gallery]] =
    collection.
      find(BSONDocument("galleryId" -> galleryId, "online" -> true)).
      one[Gallery]


  def findSimple(galleryId: Int): Future[Option[GallerySimple]] =
    collection.
      find(BSONDocument("galleryId" -> galleryId, "online" -> true)).
      one[GallerySimple]

  /**
   * Purpose of that method is to returning gallery, of the same category, that is right before a
   * gallery with passed rank. If currentRank is 0 then it will return None
   * @param categoryId Category ID
   * @param currentRank Index of the gallery in the category
   * @return
   */
  def findPreviousGalleryInCategory(categoryId: Int, currentRank: Int): Future[Option[GallerySimple]] =
    collection.
      find(BSONDocument("categoryId" -> categoryId, "online" -> true, "rank" -> BSONDocument("$lt" -> currentRank))).
      sort(BSONDocument("rank" -> -1)).
      one[GallerySimple]

  /**
   * Purpose of that method is to returning gallery, of the same category, that is right after a
   * gallery with passed rank. If currentRank has a rank higher than all its siblings, then it will return None
   * @param categoryId Category ID
   * @param currentRank Index of the gallery in the category
   * @return
   */
  def findNextGalleryInCategory(categoryId: Int, currentRank: Int): Future[Option[GallerySimple]] =
    collection.
      find(BSONDocument("categoryId" -> categoryId, "online" -> true, "rank" -> BSONDocument("$gt" -> currentRank))).
      sort(BSONDocument("rank" -> 1)).
      one[GallerySimple]

  def findLastGallerySimpleOfCategory(categoryId: Int): Future[Option[GallerySimple]] =
    collection.
      find(BSONDocument("categoryId" -> categoryId, "online" -> true)).
      sort(BSONDocument("rank" -> -1)).
      one[GallerySimple]

  def findLastGalleryOfCategory(categoryId: Int): Future[Option[Gallery]] =
    collection.
      find(BSONDocument("categoryId" -> categoryId, "online" -> true)).
      sort(BSONDocument("rank" -> -1)).
      one[Gallery]
  
  def findFirstGallerySimpleOfCategory(categoryId: Int): Future[Option[GallerySimple]] =
    collection.
      find(BSONDocument("categoryId" -> categoryId, "online" -> true)).
      sort(BSONDocument("rank" -> 1)).
      one[GallerySimple]
}
