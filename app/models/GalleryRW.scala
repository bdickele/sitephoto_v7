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
}
