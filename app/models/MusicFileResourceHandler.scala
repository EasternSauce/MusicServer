package models

import javax.inject.{Inject, Provider}
import models.dao.MusicFileRepository
import play.api.MarkerContext
import play.api.libs.json._

import scala.concurrent.{ExecutionContext, Future}

/**
  * DTO for displaying post information.
  */
case class MusicFileResource(fileId: String, fileName: String)

object MusicFileResource {

  /**
    * Mapping to write a PostResource out as a JSON value.
    */
  implicit val implicitWrites = new Writes[MusicFileResource] {
    def writes(post: MusicFileResource): JsValue = {
      Json.obj(
        "id" -> post.fileId,
        "link" -> post.fileName
      )
    }
  }
}

/**
  * Controls access to the backend data, returning [[MusicFileResource]]
  */
class PostResourceHandler @Inject()(
                                     routerProvider: Provider[MusicFileRouter],
                                     musicFileRepository: MusicFileRepository)(implicit ec: ExecutionContext) {

//  def create(musicFileInput: PostFormInput)(implicit mc: MarkerContext): Future[MusicFileResource] = {
//    val data = MusicFileData(MusicFileId("999"), musicFileInput.title, musicFileInput.body)
//    // We don't actually create the post, so return what we have
//    musicFileRepository.create(data).map { id =>
//      createPostResource(data)
//    }
//  }

//  def lookup(id: String)(implicit mc: MarkerContext): Future[Option[PostResource]] = {
//    val postFuture = postRepository.get(PostId(id))
//    postFuture.map { maybePostData =>
//      maybePostData.map { postData =>
//        createPostResource(postData)
//      }
//    }
//  }
//
  def find(implicit mc: MarkerContext): Future[Iterable[MusicFileResource]] = {
    musicFileRepository.getAll.map { musicFileDataList =>
      musicFileDataList.map(musicFileData => createPostResource(musicFileData))
    }
  }

  private def createPostResource(p: MusicFileData): MusicFileResource = {
    MusicFileResource(p.fileId.toString, p.fileName)
  }

}
