package models

import play.api.libs.json._

/**
 * All "entity" classes we need
 * bdickele
 */

case class Category(categoryId: Int,
                    rank: Int)

case class GalleryBasic(categoryId: Int,
                        galleryId: Int,
                        rank: Int)

case class Gallery(categoryId: Int,
                   galleryId: Int,
                   title: String,
                   comment: Option[String],
                   pictures: List[GalleryPic])

case class GalleryPic(thumbnail: String,
                      web: String,
                      comment: Option[String])

object Models {

  // --------------------------------------------------------------
  // Mappers (Reads)
  // --------------------------------------------------------------

  // Mapper JSON -> Category
  implicit val categoryReader = Json.reads[Category]

  // Mapper: JSON -> GalleryBasic
  implicit val gallerySimpleReader = Json.reads[GalleryBasic]

  // Mapper: JSON -> GalleryPic
  implicit val galleryPicReader = Json.reads[GalleryPic]

  // Mapper: JSON -> Gallery
  implicit val galleryReader = Json.reads[Gallery]
}
