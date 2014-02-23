package app.models

import org.specs2.mutable.Specification
import models.GalleryRW
import app.TestApplication
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration
import scala.concurrent.Await

/**
 * Created by bdickele
 * Date: 2/23/14
 */
class GalleryRWSpec extends Specification {

  "Method find" should {

    "return first gallery when passing id 1" in new TestApplication {

      val future = GalleryRW.find(1)
      val gallery = Await.result(future, Duration(5, TimeUnit.SECONDS)).get

      gallery.galleryId must equalTo(1)
      gallery.categoryId must equalTo(1)
      gallery.title must equalTo("Eté 2004 : divers")
    }

  }

}
