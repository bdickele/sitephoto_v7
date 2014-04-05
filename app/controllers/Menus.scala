package controllers

import play.api.mvc.{Action, Controller}
import play.api.libs.json.Json
import play.api.libs.concurrent.Execution.Implicits._
import service.MenuService

/**
 * Created by bdickele
 * Date: 2/22/14
 */
object Menus extends Controller {

  def menu = Action.async {
    MenuService.createMenu.map(menu => Ok(Json.toJson(menu)))
  }
}