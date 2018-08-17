package controllers

import javax.inject.{Inject, Singleton}
import models.{MusicFileData, MusicFileId}
import play.api.mvc._
import services.MusicFileRepository

import scala.concurrent.ExecutionContext

@Singleton
class HomeController @Inject()(musicService: MusicFileRepository, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def insert(id: Int, name: String, location: String): Action[AnyContent] = Action.async { implicit request =>
    musicService.create(MusicFileData(MusicFileId(id.toString), name, location)).map { _ =>
      Ok(views.html.index("Item added."))
    }
  }

  def indexAll: Action[AnyContent] = Action.async { implicit request =>
    musicService.getAll.map {
      i => Ok(views.html.list("Item retrieved.", i))
    }
  }

  def getMusicFile(id: Int) = Action {
    musicService.index(id).location
    Ok.sendFile(
      content = new java.io.File(musicService.index(id).location),
      fileName = _ => "song.mp3"
    )
  }

}
