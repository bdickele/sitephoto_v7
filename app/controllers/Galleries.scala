package controllers

import play.api.mvc.{Action, Controller}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.Logger
import models.GalleryRW

/**
 * Created by bdickele
 * Date: 2/17/14
 */
object Galleries extends Controller {

  def view(galleryId: Int) = Action.async {
    val future = galleryId match {
      case -1 => GalleryRW.findLast
      case n => GalleryRW.find(n)
    }

     future.map {
       option => option match {
         case None => {
           val message = "Could not find an online gallery for id "+ galleryId
           Logger.error(message)
           BadRequest(message)
         }
         case Some(gallery) => Ok(views.html.gallery(gallery))
       }
     }
  }
}
