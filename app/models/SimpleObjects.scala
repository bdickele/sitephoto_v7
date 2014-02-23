package models

import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONInteger}

/**
 * Created by bdickele
 * Date: 2/23/14
 */
case class GallerySimple(categoryId: Int,
                         galleryId: Int,
                         rank: Int)

case class CategorySimple(categoryId: Int,
                          rank: Int)

object GallerySimple {

  implicit object GallerySimpleBSONHandler extends BSONDocumentReader[GallerySimple] {

    def read(doc: BSONDocument) =
      GallerySimple(
        doc.getAs[BSONInteger]("categoryId").get.value,
        doc.getAs[BSONInteger]("galleryId").get.value,
        doc.getAs[BSONInteger]("rank").get.value)

  }

}

object CategorySimple {

  implicit object CategorySimpleBSONHandler extends BSONDocumentReader[CategorySimple] {

    def read(doc: BSONDocument) =
      CategorySimple(
        doc.getAs[BSONInteger]("categoryId").get.value,
        doc.getAs[BSONInteger]("rank").get.value)

  }

}