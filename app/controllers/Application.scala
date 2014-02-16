package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Redirect(category.routes.Categories.view(-1))
  }

}