package models

import scala.Some

/**
 * Object standing for a gallery to display (Gallery) and for misc purposes (xxxSimple)
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
                   pictures: List[GalleryPic]) {

  val commentOrEmpty = comment match {
    case None => ""
    case Some(s) => s
  }
}

case class GalleryPic(thumbnail: String,
                      web: String,
                      comment: Option[String]) {

  val commentOrEmpty = comment match {
    case None => ""
    case Some(s) => s
  }
}
