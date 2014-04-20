package controllers

import models._
import scala.Some
import scala.concurrent.{Future, Await}
import scala.concurrent.duration._
import play.api.mvc.{SimpleResult, Action, Controller}
import play.api.libs.concurrent.Execution.Implicits._
import service.{CategoryService, GalleryService}

/**
 * Controller related to galleries
 * bdickele
 */
object Galleries extends Controller {

  /**
   * Main page
   * @param galleryId Gallery ID
   * @return
   */
  def view(galleryId: Int) = Action.async {
    val future = galleryId match {
      case -1 => GalleryService.findDefault
      case n => GalleryService.find(n)
    }

    future.map {
      case None => couldNotFindGallery(galleryId)
      case Some(gallery) => Ok(views.html.gallery(gallery))
    }
  }

  /**
   * Redirect to previous gallery of passed gallery ID
   * @param galleryId Gallery ID
   * @return
   */
  def previous(galleryId: Int) = Action.async {
    val future = GalleryService.findBasic(galleryId)

    future.flatMap {
      case None => Future.successful(couldNotFindGallery(galleryId))

      case Some(gallery) =>
        val previousFuture = GalleryService.findPreviousGalleryInCategory(gallery.categoryId, gallery.rank)
        val previousGallery: Future[GalleryBasic] = previousFuture.map {
          case Some(g) => g
          case None => lastGalleryOfPreviousCategory(gallery.categoryId)
        }

        previousGallery.map(g => Redirect(routes.Galleries.view(g.galleryId)))
    }
  }

  /**
   * Retrieves ID of last gallery of category before category of passed categoryId.
   * If categoryId stands for first category, then last category (the most recent) is selected
   * @param categoryId Category ID
   */
  def lastGalleryOfPreviousCategory(categoryId: Int): GalleryBasic = {

    // Categories are sorted by rank, what means the most recent one is the first
    val categories: List[Category] = Await.result(CategoryService.findAll, 5 seconds)

    val category = categories.find(_.categoryId == categoryId).get
    val rank = category.rank

    val newCategoryId: Int = rank match {
      case 0 => categories.head.categoryId
      case n => categories.find(_.rank < category.rank).get.categoryId
    }

    val futureGallery = GalleryService.findLastGalleryBasicOfCategory(newCategoryId)

    // In case category doesn't contain any gallery
    Await.result(futureGallery, 5 seconds) match {
      case None => lastGalleryOfPreviousCategory(newCategoryId)
      case Some(g) => g
    }
  }

  /**
   * Redirect to next gallery of passed gallery ID
   * @param galleryId Gallery ID
   * @return
   */
  def next(galleryId: Int) = Action.async {
    val future = GalleryService.findBasic(galleryId)

    future.flatMap {
      case None => Future.successful(couldNotFindGallery(galleryId))

      case Some(gallery) =>
        val nextFuture = GalleryService.findNextGalleryInCategory(gallery.categoryId, gallery.rank)
        val nextGallery: Future[GalleryBasic] = nextFuture.map {
          case Some(g) => g
          case None => firstGalleryOfNextCategory(gallery.categoryId)
        }

        nextGallery.map(g => Redirect(routes.Galleries.view(g.galleryId)))
    }
  }

  /**
   * Retrieves ID of first gallery of category after category of passed categoryId.
   * If categoryId stands for last category, then first category (the oldest) is selected
   * @param categoryId Category ID
   */
  def firstGalleryOfNextCategory(categoryId: Int): GalleryBasic = {
    // Categories are sorted by rank, what means the most recent one is the first: let's reverse it
    val categories: List[Category] = Await.result(CategoryService.findAll, 5 seconds).reverse

    val categoryIndex = categories.indexWhere(_.categoryId == categoryId)
    val lastIndex = categories.length - 1

    val newCategoryId: Int = categoryIndex match {
      case `lastIndex` => categories.head.categoryId
      case n => categories.apply(categoryIndex + 1).categoryId
    }

    val futureGallery = GalleryService.findFirstGalleryBasicOfCategory(newCategoryId)

    // In case category doesn't contain any gallery
    Await.result(futureGallery, 5 seconds) match {
      case None => firstGalleryOfNextCategory(newCategoryId)
      case Some(g) => g
    }
  }

  def couldNotFindGallery(galleryId: Int): SimpleResult =
    BadRequest(views.html.badRequest("Could not find an online gallery for id " + galleryId))
}
