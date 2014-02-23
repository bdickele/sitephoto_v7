package app.models

import org.specs2.mutable.Specification
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit
import app.TestApplication
import models.{MenuRW, MenuItemCategory}

/**
 * Created by bdickele
 * Date: 2/23/14
 */
class MenuRWSpec extends Specification {

  "Method menu" should {

    lazy val future = MenuRW.createMenu()
    lazy val categories: List[MenuItemCategory] = Await.result(future, Duration(5, TimeUnit.SECONDS))

    "return complete menu" in new TestApplication {

      categories.length must equalTo(5)

      val firstCategory = categories.head
      firstCategory.label must equalTo("2008")

      val lastCategory = categories.last
      lastCategory.label must equalTo("2004")

      val galleries = lastCategory.children.get
      galleries.length must equalTo(2)

      val leaf = galleries.head
      leaf.label must equalTo("Août 2004 : Mantoue et Venise")
      leaf.id must equalTo(2)
    }
  }
}
