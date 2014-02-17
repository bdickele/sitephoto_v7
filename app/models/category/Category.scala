package models.category

import reactivemongo.bson._
import play.api.libs.json.{JsValue, Json, Writes}
import com.fasterxml.jackson.annotation.JsonValue

/**
 * Created by bdickele
 * Date: 2/16/14
 */
case class Category(categoryId: Int,
                    title: String)

object Category {

  implicit object CategoryBSONHandler extends BSONDocumentReader[Category] {

    def read(doc: BSONDocument): Category =
      Category(
        doc.getAs[BSONInteger]("categoryId").get.value,
        doc.getAs[BSONString]("title").get.value)
  }

  implicit object CategoryJSONHandler extends Writes[Category] {

    def writes(category: Category) = Json.obj(
      "id" -> Json.toJson(category.categoryId),
      "title" -> Json.toJson(category.title)
    )
  }
}
