package models

import reactivemongo.bson._
import util.Const
import scala.Some

/**
 * Created by bdickele
 * Date: 2/18/14
 */
case class Gallery(categoryId: Int,
                   galleryId: Int,
                   title: String,
                   comment: String,
                   pictures: List[GalleryPic])

case class GalleryPic(thumbnail: String,
                      web: String,
                      comment: String)

object Gallery {

  implicit object GalleryBSONHandler extends BSONDocumentReader[Gallery] {

    def read(doc: BSONDocument): Gallery = {

      def readPicture(doc: BSONDocument): GalleryPic =
        GalleryPic(
          Const.WebRoot + doc.getAs[BSONString]("thumbnail").get.value,
          Const.WebRoot + doc.getAs[BSONString]("web").get.value,
          doc.getAs[BSONString]("comment") match {
            case None => ""
            case Some(s) => s.value
          })

      def readPictures(option: Option[BSONArray]): List[GalleryPic] = option match {
        case None => List()
        case Some(array) => {
          val stream: Stream[BSONValue] = array.values
          stream.toList.map(value => readPicture(value.asInstanceOf[BSONDocument]))
        }
      }

      Gallery(
        doc.getAs[BSONInteger]("categoryId").get.value,
        doc.getAs[BSONInteger]("galleryId").get.value,
        doc.getAs[BSONString]("title").get.value,
        doc.getAs[BSONString]("comment") match {
          case None => ""
          case Some(s) => s.value
        },
        readPictures(doc.getAs[BSONArray]("pictures")))
    }
  }

}
