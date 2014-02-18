package models.gallery

import _root_.util.Const
import reactivemongo.bson._
import reactivemongo.bson.BSONString
import reactivemongo.bson.BSONInteger
import play.api.libs.json.{Json, Writes}

/**
 * Created by bdickele
 * Date: 2/16/14
 */
case class GalleryBasic(galleryId: Int,
                        title: String,
                        thumbnail: String)

object GalleryBasic {

  implicit object GalleryBasicBSONHandler extends BSONDocumentReader[GalleryBasic] {

    def read(doc: BSONDocument): GalleryBasic =
      GalleryBasic(
        doc.getAs[BSONInteger]("galleryId").get.value,
        doc.getAs[BSONString]("title").get.value,
        Const.WebRoot + doc.getAs[BSONString]("thumbnail").get.value)
  }

  implicit object GalleryBasicJSONHandler extends Writes[GalleryBasic] {

    def writes(gallery: GalleryBasic) = Json.obj(
      "galleryId" -> Json.toJson(gallery.galleryId),
      "title" -> Json.toJson(gallery.title),
      "thumbnail" -> Json.toJson(gallery.thumbnail)
    )
  }

}
