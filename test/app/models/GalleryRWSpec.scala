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

  "Method findDefault" should {

    "return most recent oneline gallery" in new TestApplication {

      val future = GalleryRW.findDefault
      val gallery = Await.result(future, Duration(5, TimeUnit.SECONDS)).get

      gallery.galleryId must equalTo(17)
      gallery.categoryId must equalTo(3)
      gallery.title must equalTo("Décembre 2006 : Montpellier, Nîmes")
    }
  }

  "Methods find (by gallery ID)" should {

    "return first Gallery when passing id 1" in new TestApplication {

      val future = GalleryRW.find(1)
      val gallery = Await.result(future, Duration(5, TimeUnit.SECONDS)).get

      gallery.galleryId must equalTo(1)
      gallery.categoryId must equalTo(1)
      gallery.title must equalTo("Eté 2004 : divers")
    }

    "return first GallerySimple when passing id 1" in new TestApplication {

      val future = GalleryRW.findSimple(1)
      val gallery = Await.result(future, Duration(5, TimeUnit.SECONDS)).get

      gallery.galleryId must equalTo(1)
      gallery.categoryId must equalTo(1)
    }
  }

  "Method findPreviousGalleryInCategory" should {

    "return previous gallery of same category when passed rank is > 0" in new TestApplication {
      val future = GalleryRW.findPreviousGalleryInCategory(1, 1)
      val gallery = Await.result(future, Duration(5, TimeUnit.SECONDS)).get

      gallery.galleryId must equalTo(1)
      gallery.categoryId must equalTo(1)
    }

    "return None when passed rank is 0" in new TestApplication {
      val future = GalleryRW.findPreviousGalleryInCategory(1, 0)
      val option = Await.result(future, Duration(5, TimeUnit.SECONDS))

      option must equalTo(None)
    }
  }

  "Method findNextGalleryInCategory" should {

    "return previous gallery of same category when passed rank is < max rank" in new TestApplication {
      val future = GalleryRW.findNextGalleryInCategory(1, 0)
      val gallery = Await.result(future, Duration(5, TimeUnit.SECONDS)).get

      gallery.galleryId must equalTo(2)
      gallery.categoryId must equalTo(1)
    }

    "return None when passed rank is = max rank" in new TestApplication {
      val future = GalleryRW.findNextGalleryInCategory(1, 1)
      val option = Await.result(future, Duration(5, TimeUnit.SECONDS))

      option must equalTo(None)
    }
  }

  "Method findFirstGallerySimpleOfCategory" should {

    "return Eté 2004 : divers when category ID is 1" in new TestApplication {
      val future = GalleryRW.findFirstGallerySimpleOfCategory(1)
      val gallery = Await.result(future, Duration(5, TimeUnit.SECONDS)).get

      gallery.galleryId must equalTo(1)
      gallery.categoryId must equalTo(1)
    }
  }

  "Method findLastGallerySimpleOfCategory" should {

    "return Août 2004 : Mantoue et Venise when category ID is 1" in new TestApplication {
      val future = GalleryRW.findLastGallerySimpleOfCategory(1)
      val gallery = Await.result(future, Duration(5, TimeUnit.SECONDS)).get

      gallery.galleryId must equalTo(2)
      gallery.categoryId must equalTo(1)
    }
  }

  "Method findLastGalleryOfCategory" should {

    "return Août 2004 : Mantoue et Venise when category ID is 1" in new TestApplication {
      val future = GalleryRW.findLastGalleryOfCategory(1)
      val gallery = Await.result(future, Duration(5, TimeUnit.SECONDS)).get

      gallery.galleryId must equalTo(2)
      gallery.categoryId must equalTo(1)
      gallery.title must equalTo("Août 2004 : Mantoue et Venise")
    }
  }

}
