package controllers.gallery

import play.api.mvc.{Action, Controller}
import play.api.libs.json.Json
import models.gallery.GalleryBasicRW
import play.api.libs.concurrent.Execution.Implicits._

/**
 * Created by bdickele
 * Date: 2/17/14
 */
object Galleries extends Controller {

  /**
   * @return List of galleries (basic) as JSON list
   */
  def listBasic(categoryId: Int) = Action.async {
    GalleryBasicRW.findAll(categoryId).map {
      gallery =>
        Ok(Json.toJson(gallery))
    }
  }

  def view(galleryId: Int) = TODO
}
