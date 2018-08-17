package models


final case class MusicFileData(fileId: MusicFileId, fileName: String, location: String)

class MusicFileId private (val underlying: Int) extends AnyVal {
  override def toString: String = underlying.toString
}

object MusicFileId {
  def apply(raw: String): MusicFileId = {
    require(raw != null)
    new MusicFileId(Integer.parseInt(raw))
  }
}
