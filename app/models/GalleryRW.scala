package models

import play.api.mvc.Controller
import play.modules.reactivemongo.MongoController
import reactivemongo.api.collections.default.BSONCollection
import scala.concurrent.Future
import reactivemongo.bson.BSONDocument
import play.api.libs.concurrent.Execution.Implicits._

/**
 * Created by bdickele
 * Date: 2/18/14
 */
object GalleryRW extends Controller with MongoController {

  def collection = db.collection[BSONCollection]("gallery")

  def findLast: Future[Option[Gallery]] =
    collection.
      find(BSONDocument("online" -> true)).
      sort(BSONDocument("rank" -> -1)).
      one[Gallery]

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
   * @param categoryId
   * @param currentRank
   * @return
   */
  def findPreviousGalleryInCategory(categoryId: Int, currentRank: Int): Future[Option[GallerySimple]] =
    collection.
      find(BSONDocument("categoryId" -> categoryId, "rank" -> BSONDocument("$lt" -> currentRank))).
      sort(BSONDocument("rank" -> -1)).
      one[GallerySimple]

  def findLastGalleryOfCategory(categoryId: Int): Future[Option[GallerySimple]] =
    collection.
      find(BSONDocument("categoryId" -> categoryId)).
      sort(BSONDocument("rank" -> -1)).
      one[GallerySimple]

  /**
   * Purpose of that method is to returning gallery, of the same category, that is right after a
   * gallery with passed rank. If currentRank has a rank higher than all its siblings, then it will return None
   * @param categoryId
   * @param currentRank
   * @return
   */
  def findNextGalleryInCategory(categoryId: Int, currentRank: Int): Future[Option[GallerySimple]] =
    collection.
      find(BSONDocument("categoryId" -> categoryId, "rank" -> BSONDocument("$gt" -> currentRank))).
      sort(BSONDocument("rank" -> 1)).
      one[GallerySimple]

  def findFirstGalleryOfCategory(categoryId: Int): Future[Option[GallerySimple]] =
    collection.
      find(BSONDocument("categoryId" -> categoryId)).
      sort(BSONDocument("rank" -> 1)).
      one[GallerySimple]
}
