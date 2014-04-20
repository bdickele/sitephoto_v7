package app.models

import org.specs2.mutable.Specification
import app.TestApplication
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit
import service.CategoryService


class CategoryServiceSpec extends Specification {

  "Method findAll" should {

    "return all online categories" in new TestApplication {

      val future = CategoryService.findAll
      val categories = Await.result(future, Duration(5, TimeUnit.SECONDS))

      categories.length must equalTo(11)
      categories.head.categoryId must equalTo(11)
      categories.last.categoryId must equalTo(1)
    }
  }
}
