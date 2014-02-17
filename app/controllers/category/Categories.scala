package controllers.category

import play.api.mvc.{Action, Controller}
import models.category.CategoryRW
import play.api.Logger
import play.api.libs.json.Json
import play.api.libs.concurrent.Execution.Implicits._

/**
 * Created by bdickele
 * Date: 2/16/14
 */
object Categories extends Controller {

  def view(categoryId: Int) = Action.async {
    val future = categoryId match {
      case -1 => CategoryRW.findFirst
      case n => CategoryRW.find(n)
    }

    future.map {
      option => option match {
        case None => {
          Logger.error("Incorrect passed categoryId for list of categories")
          BadRequest("Incorrect passed categoryId for list of categories")
        }
        case Some(category) => Ok(views.html.category.category(category.categoryId, category.title))
      }
    }
  }

  /**
   * @return List of categories as JSON list
   */
  def list = Action.async {
    CategoryRW.findAll.map {
      category =>
        Ok(Json.toJson(category))
    }
  }

}
