package controllers.category

import play.api.mvc.{Action, Controller}

/**
 * Created by bdickele
 * Date: 2/16/14
 */
object Categories extends Controller {

  def view(categoryId: Int) = Action {
    Ok(views.html.category.category())
  }

}
