package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Redirect(category.routes.Categories.view(-1))
  }

  /**
   * Javacript routes for javascript and coffeescript files
   * @return
   */
  def javascriptRoutes = Action {
    implicit request =>
      Ok(Routes.javascriptRouter("jsRoutes")(
        controllers.category.routes.javascript.Categories.view,
        controllers.gallery.routes.javascript.Galleries.view)).as("text/javascript")
  }

}