package models.gallery

import play.api.mvc.Controller
import play.modules.reactivemongo.MongoController
import reactivemongo.api.collections.default.BSONCollection
import scala.concurrent.Future
import reactivemongo.bson.BSONDocument
import play.api.libs.concurrent.Execution.Implicits._

/**
 * Created by bdickele
 * Date: 2/16/14
 */
object GalleryBasicRW extends Controller with MongoController {

  def collection = db.collection[BSONCollection]("gallery")

  // --------------------------------------------------------------
  // FIND
  // --------------------------------------------------------------

  def findAll(categoryId: Int): Future[List[GalleryBasic]] =
    collection.
      find(BSONDocument("categoryId" -> categoryId, "online" -> true)).
      sort(BSONDocument("rank" -> -1)).
      cursor[GalleryBasic].
      collect[List]()

}
