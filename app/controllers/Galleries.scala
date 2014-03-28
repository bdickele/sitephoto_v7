package controllers

import play.api.mvc.{Action, Controller}
import models._
import scala.concurrent.{Future, Await}
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit
import play.api.libs.concurrent.Execution.Implicits._
import scala.Some

/**
 * Created by bdickele
 * Date: 2/17/14
 */
object Galleries extends Controller {

  /**
   * Main page
   * @param galleryId Gallery ID
   * @return
   */
  def view(galleryId: Int) = Action.async {
    val future = galleryId match {
      case -1 => GalleryRW.findDefault
      case n => GalleryRW.find(n)
    }

    future.map {
      option => option match {
        case None => BadRequest(views.html.badRequest("Could not find an online gallery for id " + galleryId))
        case Some(gallery) => Ok(views.html.gallery(gallery))
      }
    }
  }

  /**
   * Redirect to previous gallery of passed gallery ID
   * @param galleryId Gallery ID
   * @return
   */
  def previous(galleryId: Int) = Action.async {
    val optionGallery = Await.result(GalleryRW.findSimple(galleryId), Duration(5, TimeUnit.SECONDS))

    optionGallery match {
      case None => Future.successful(BadRequest(views.html.badRequest("Could not find an online gallery for id " + galleryId)))
      case Some(gallery) => {
        val previousFuture = GalleryRW.findPreviousGalleryInCategory(gallery.categoryId, gallery.rank)
        val previousGallery: Future[GallerySimple] = previousFuture.map {
          option => option match {
            case Some(g) => g
            case None => lastGalleryOfPreviousCategory(gallery.categoryId)
          }
        }

        previousGallery.map(g => Redirect(routes.Galleries.view(g.galleryId)))
      }
    }
  }

  /**
   * Retrieves ID of last gallery of category before category of passed categoryId.
   * If categoryId stands for first category, then last category (the most recent) is selected
   * @param categoryId Category ID
   */
  def lastGalleryOfPreviousCategory(categoryId: Int): GallerySimple = {
    // Categories are sorted by rank, what means the most recent one is the first
    val categories: List[CategorySimple] = Await.result(CategoryRW.findAll, Duration(5, TimeUnit.SECONDS))

    val category = categories.find(_.categoryId == categoryId).get
    val rank = category.rank

    val newCategoryId: Int = rank match {
      case 0 => categories.head.categoryId
      case n => categories.find(_.rank < category.rank).get.categoryId
    }

    val futureGallery = GalleryRW.findLastGallerySimpleOfCategory(newCategoryId)

    // In case category doesn't contain any gallery
    Await.result(futureGallery, Duration(5, TimeUnit.SECONDS)) match {
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
    val optionGallery = Await.result(GalleryRW.findSimple(galleryId), Duration(5, TimeUnit.SECONDS))

    optionGallery match {
      case None => Future.successful(BadRequest(views.html.badRequest("Could not find an online gallery for id " + galleryId)))
      case Some(gallery) => {
        val nextFuture = GalleryRW.findNextGalleryInCategory(gallery.categoryId, gallery.rank)
        val nextGallery: Future[GallerySimple] = nextFuture.map {
          option => option match {
            case Some(g) => g
            case None => firstGalleryOfNextCategory(gallery.categoryId)
          }
        }

        nextGallery.map(g => Redirect(routes.Galleries.view(g.galleryId)))
      }
    }
  }

  /**
   * Retrieves ID of first gallery of category after category of passed categoryId.
   * If categoryId stands for last category, then first category (the oldest) is selected
   * @param categoryId Category ID
   */
  def firstGalleryOfNextCategory(categoryId: Int): GallerySimple = {
    // Categories are sorted by rank, what means the most recent one is the first: let's reverse it
    val categories: List[CategorySimple] = Await.result(CategoryRW.findAll, Duration(5, TimeUnit.SECONDS)).reverse

    val categoryIndex = categories.indexWhere(_.categoryId == categoryId)
    val lastIndex = categories.length - 1

    val newCategoryId: Int = categoryIndex match {
      case `lastIndex` => categories.head.categoryId
      case n => categories.apply(categoryIndex + 1).categoryId
    }

    val futureGallery = GalleryRW.findFirstGallerySimpleOfCategory(newCategoryId)

    // In case category doesn't contain any gallery
    Await.result(futureGallery, Duration(5, TimeUnit.SECONDS)) match {
      case None => firstGalleryOfNextCategory(newCategoryId)
      case Some(g) => g
    }
  }
}
