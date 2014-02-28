package models

import play.api.mvc.Controller
import play.modules.reactivemongo.MongoController
import play.api.libs.concurrent.Execution.Implicits._
import reactivemongo.api.collections.default.BSONCollection
import scala.concurrent.Future
import reactivemongo.bson.BSONDocument

/**
 * Created by bdickele
 * Date: 2/23/14
 */
object CategoryRW extends Controller with MongoController {

  def collection = db.collection[BSONCollection]("category")


  /** @return complete list of categories from DB, sorted from the
    *         category with greater rank to lower rank */
  def findAll: Future[List[CategorySimple]] =
    collection.
      find(BSONDocument("online" -> true)).
      sort(BSONDocument("rank" -> -1)).
      cursor[CategorySimple].
      collect[List]()
}
