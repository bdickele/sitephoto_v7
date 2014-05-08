package models

import _root_.util.Const
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
                   pictures: List[GalleryPic]) {

  // Builds a version of Category whose path for thumbnail and web format pictures
  // includes URL "http://...", so that pictures can be displayed in the web site
  def buildForFrontEnd = copy(pictures = pictures.map(_.buildForFrontEnd))
}

case class GalleryPic(thumbnail: String,
                      web: String,
                      print: Option[String],
                      comment: Option[String]) {

  lazy val name = web.substring(web.lastIndexOf("/") + 1)

  // Builds a version of GalleryPic whose path for thumbnail and web format pictures
  // includes URL "http://...", so that pictures can be displayed in the web site`
  // If there is no print version, then "print" will be equal to web version
  def buildForFrontEnd = copy(
    thumbnail = Const.PhotoStockRoot + this.thumbnail,
    web = Const.PhotoStockRoot + this.web,
    print = this.print match {
      case None => Some(Const.PhotoStockRoot + this.web)
      case Some(s) => Some(Const.PhotoStockRoot + s)
    })
}


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
