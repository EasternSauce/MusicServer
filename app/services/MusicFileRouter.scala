package services

import controllers.MusicFileController
import javax.inject.Inject
import models.MusicFileId
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

/**
  * Routes and URLs to the PostResource controller.
  */
class MusicFileRouter @Inject()(controller: MusicFileController) extends SimpleRouter {
  val prefix = "/musicfiles"

  def link(id: MusicFileId): String = {
    import com.netaporter.uri.dsl._
    val url = prefix / id.toString
    url.toString()
  }

  override def routes: Routes = {
    case GET(p"/") =>
      controller.index
  }

}
