package controllers

import javax.inject.Inject
import play.api.Logger
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc._
import services.{MusicFileBaseController, MusicFileControllerComponents}

import scala.concurrent.ExecutionContext

case class PostFormInput(title: String, body: String)

/**
  * Takes HTTP requests and produces JSON.
  */
class MusicFileController @Inject()(cc: MusicFileControllerComponents)(implicit ec: ExecutionContext)
    extends MusicFileBaseController(cc) {

  private val logger = Logger(getClass)

  private val form: Form[PostFormInput] = {
    import play.api.data.Forms._

    Form(
      mapping(
        "title" -> nonEmptyText,
        "body" -> text
      )(PostFormInput.apply)(PostFormInput.unapply)
    )
  }

  def index: Action[AnyContent] = PostAction.async { implicit request =>
    logger.trace("index: ")
    postResourceHandler.find.map { posts =>
      Ok(Json.toJson(posts))
    }
  }

}
