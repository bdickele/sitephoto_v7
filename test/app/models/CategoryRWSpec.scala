package app.models

import org.specs2.mutable.Specification
import app.TestApplication
import models.CategoryRW
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit

/**
 * Created by bdickele
 * Date: 2/28/14
 */
class CategoryRWSpec extends Specification {

  "Method findAll" should {

    "return all online categories (as CategorySimple)" in new TestApplication {

      val future = CategoryRW.findAll
      val categories = Await.result(future, Duration(5, TimeUnit.SECONDS))

      categories.length must equalTo(5)
      categories.head.categoryId must equalTo(5)
    }

  }

}
