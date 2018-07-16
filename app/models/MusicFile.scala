package models

import play.api.libs.functional.syntax._
import play.api.libs.json._


case class MusicFileItem(fileId: Int, fileName: String)

object MusicFile {
  implicit val musicFileWrites: Writes[MusicFileItem] = (
    (JsPath \ "fileId").write[Int] and
      (JsPath \ "fileName").write[String]
    )(unlift(MusicFileItem.unapply))
}