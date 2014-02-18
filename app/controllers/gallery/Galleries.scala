package controllers.gallery

import play.api.mvc.{Action, Controller}
import play.api.libs.json.Json
import models.gallery.GalleryRW
import play.api.libs.concurrent.Execution.Implicits._
import play.api.Logger

/**
 * Created by bdickele
 * Date: 2/17/14
 */
object Galleries extends Controller {

  /**
   * @return List of galleries (basic) as JSON list
   */
  def listBasic(categoryId: Int) = Action.async {
    GalleryRW.findAllBasic(categoryId).map {
      gallery =>
        Ok(Json.toJson(gallery))
    }
  }

  def view(galleryId: Int) = Action.async {
     GalleryRW.find(galleryId).map {
       option => option match {
         case None => {
           val message = "Could not find an online gallery for id "+ galleryId
           Logger.error(message)
           BadRequest(message)
         }
         case Some(gallery) => Ok(views.html.gallery.gallery(gallery))
       }
     }
  }
}
