package models.category

import play.api.mvc.Controller
import play.modules.reactivemongo.MongoController
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.BSONDocument
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._

/**
 * Created by bdickele
 * Date: 2/16/14
 */
object CategoryRW extends Controller with MongoController {

  def collection = db.collection[BSONCollection]("category")

  val findAllQuery = BSONDocument("online" -> true)


  def findFirst: Future[Option[Category]] =
    collection.
      find(findAllQuery).
      sort(BSONDocument("rank" -> -1)).
      one[Category]

  def find(categoryId: Int): Future[Option[Category]] =
    collection.
      find(BSONDocument("online" -> true, "categoryId" -> categoryId)).
      one[Category]

  /** @return complete list of categories from DB, sorted from the
    *         category with greater rank to lower rank */
  def findAll: Future[List[Category]] =
    collection.
      find(findAllQuery).
      sort(BSONDocument("rank" -> -1)).
      cursor[Category].
      collect[List]()

}
