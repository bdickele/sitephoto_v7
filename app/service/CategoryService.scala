package service

import play.api.mvc.Controller
import play.modules.reactivemongo.MongoController
import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.Future
import models.Category
import models.Models._
import play.api.libs.json._
import play.modules.reactivemongo.json.collection.JSONCollection

/**
 * Service related to categories
 * bdickele
 */
object CategoryService extends Controller with MongoController {

  def collection = db.collection[JSONCollection]("category")


  /** @return complete list of categories from DB, sorted from the
    *         category with greater rank to lower rank */
  def findAll: Future[List[Category]] =
    collection.
      find(Json.obj("online" -> true)).
      sort(Json.obj("rank" -> -1)).
      cursor[Category].
      collect[List]()
}
