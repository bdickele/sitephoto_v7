import play.api._
import play.api.mvc._
import play.api.mvc.Results._
import scala.concurrent.Future


/**
 * Overriding GlobalSettings to customize error pages for instance
 * Created by bdickele on 27/03/14.
 */
object Global extends GlobalSettings {

  override def onHandlerNotFound(request: RequestHeader) = {
    Future.successful(NotFound(
      views.html.notFound(request.path)
    ))
  }

  override def onError(request: RequestHeader, ex: Throwable) = {
    Future.successful(BadRequest(
      views.html.badRequest(ex.getMessage)
    ))
  }

}
