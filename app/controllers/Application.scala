package controllers

import play.api._
import play.api.mvc._

/**
 * Application controller
 * bdickele
 */
object Application extends Controller {

  def index = Action {
    Redirect(routes.Galleries.view(-1))
  }

  /**
   * Javacript routes for javascript and coffeescript files
   * @return
   */
  def javascriptRoutes = Action {
    implicit request =>
      Ok(Routes.javascriptRouter("jsRoutes")(
        controllers.routes.javascript.Galleries.view)).as("text/javascript")
  }

}