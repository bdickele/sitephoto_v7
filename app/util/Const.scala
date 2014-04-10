package util

import play.api.Play


/**
 * Constants
 * bdickele
 */
object Const {

  val PhotoStockRoot = Play.current.configuration.getString("photostock.root").get

  val FolderWeb = "web/"
  val FolderThumbnail = "thumbnail/"
  val FolderPrint = "print/"

}
