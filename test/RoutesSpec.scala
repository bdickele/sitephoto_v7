import app.TestApplication
import java.util.concurrent.TimeUnit
import org.specs2.mutable._
import play.api.libs.ws.{Response, WS}
import play.api.test.Helpers._
import scala.concurrent.duration.Duration
import scala.concurrent.{Future, Await}


class RoutesSpec extends Specification {

  val urlPrefix = "http://localhost:9000"
  val callbackURL = urlPrefix + "/callback"

  private def futureURL(url: String): Future[Response] =
    WS.url(urlPrefix + "/"+ url).withQueryString("callbackURL" -> callbackURL).get()

  private def responseURL(url: String) =
    Await.result(futureURL(url), Duration(5, TimeUnit.SECONDS))


  "routes" should {

    "return 200 (OK) for correct URLs" in new TestApplication {
      var response = responseURL("gallery")
      response.status must equalTo(OK)

      response = responseURL("gallery/1")
      response.status must equalTo(OK)
    }

    "return 400 (Bad request) for incorrect parameters in URL" in new TestApplication {
      val response = responseURL("gallery/999") // Incorrect gallery ID
      response.status must equalTo(BAD_REQUEST)
    }

    "return 404 (Page not found) for incorrect URLs" in new TestApplication {
      val response = responseURL("whatever")
      response.status must equalTo(NOT_FOUND)
    }

  }
}
