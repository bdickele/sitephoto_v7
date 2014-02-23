package models

import play.api.libs.json.{JsArray, JsNull, Json, Writes}


/**
 * Created by bdickele
 * Date: 2/22/14
 */
case class MenuItemCategory(label: String,
                            children: Option[List[MenuItemGallery]])

case class MenuItemGallery(label: String,
                           id: Int)

object MenuItemCategory {

  implicit object MenuJSONHandler extends Writes[MenuItemCategory] {

    def writes(item: MenuItemCategory) = Json.obj(
      "label" -> Json.toJson(item.label),
      "children" -> {
        item.children match {
          case None => JsNull
          case Some(children) => JsArray(children.map(g =>
            Json.obj("label" -> Json.toJson(g.label), "id" -> Json.toJson(g.id))))
        }
      }
    )
  }

}