package models.dao

import anorm.SqlParser.{scalar, _}
import anorm._
import javax.inject.Inject
import models.{DatabaseExecutionContext, MusicFileItem}
import play.api.db._

import scala.concurrent.Future

class MusicFileRepository @Inject()(dbApi: DBApi)(implicit ec: DatabaseExecutionContext) {
  val db = dbApi.database("default")

  def create(musicFile: MusicFileItem) = Future {
    db.withConnection { implicit c =>
      SQL(
        """
          | INSERT IGNORE INTO `music_files` (`file_id`, `file_name`)
          | VALUES
          |   ({file_id}, {file_name});
        """.stripMargin).on(
        "file_id" -> musicFile.fileId,
        "file_name" -> musicFile.fileName
      ).executeInsert()
    }
  }(ec)

  def delete(musicFile: MusicFileItem) = {
    db.withConnection { implicit c =>
      SQL(
        """
          | DELETE FROM `music_files`
          | WHERE `file_id`={file_id} AND `file_name`={file_name}
          | LIMIT 1;
        """.stripMargin).on(
        "file_id" -> musicFile.fileId,
        "file_name" -> musicFile.fileName
      ).executeUpdate()
    }
  }

  def exists(musicFile: MusicFileItem): Boolean = {
    db.withConnection { implicit c =>
      val parser = scalar[Int].single
      val result = SQL(
        """
          | SELECT COUNT(*) as numMatches
          | FROM `music_files`
          | WHERE `file_id`={file_id} AND `file_name`={file_name};
        """.stripMargin).on(
        "file_id" -> musicFile.fileId,
        "file_name" -> musicFile.fileName
      ).as(parser)

      result == 0
    }
  }

  def index(file_id: Int): MusicFileItem = {
    db.withConnection { implicit c =>
      val result: Option[Int ~ String] = SQL(
        """
          | SELECT `file_id`, `file_name`
          | FROM `music_files`
          | WHERE `file_id`={file_id};
        """.stripMargin).on(
        "file_id" -> file_id
      ).as((int("file_id") ~ str("file_name")).singleOpt)

      MusicFileItem(result.get._1,result.get._2)

    }
  }

  def getAll: Future[List[MusicFileItem]] = Future {
    db.withConnection { implicit c =>
      val result: List[Int ~ String] = SQL(
        """
          SELECT file_id, file_name from music_files;
              """
      ).executeQuery().as((int("file_id") ~ str("file_name")).*)

      result.collect { case i => MusicFileItem(i._1, i._2) }
    }
  }(ec)

}
