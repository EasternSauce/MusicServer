package controllers

import javax.inject.{Inject, Singleton}
import models.MusicFileItem
import models.dao.MusicFileRepository
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class HomeController @Inject()(musicService: MusicFileRepository, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def insert(id: Int, name: String): Action[AnyContent] = Action.async { implicit request =>
    musicService.create(MusicFileItem(id, name)).map { _ =>
      Ok(views.html.index("Item added."))
    }
  }

  def retrieve: Action[AnyContent] = Action.async { implicit request =>
    musicService.getAll.map { i => Ok(views.html.index("Item retrieved: " + i.toList ))
    }
  }



}
