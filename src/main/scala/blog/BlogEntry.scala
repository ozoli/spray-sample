package blog

import slick.driver.H2Driver.api._
import com.github.tototoshi.slick.H2JodaSupport._
import slick.lifted.{ProvenShape, ForeignKeyQuery}

import org.joda.time.DateTime

/**
 * Created by ocarr on 31/08/15.
 */
case class BlogEntry(id: Long, pubDate: DateTime, title: String, body: String, category: String)

// A BlogEntries table with 5 columns: id, pubDate, title, body, category
class BlogEntries(tag: Tag) extends Table[BlogEntry](tag, "BLOGENTRIES") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def pubDate = column[DateTime]("PUB_DATE")
  def title = column[String]("TITLE")
  def body = column[String]("BODY")
  def category = column[String]("CATEGORY")

  def * = (id, pubDate, title, body, category) <> (BlogEntry.tupled, BlogEntry.unapply _)
}
