import blog.{RssReader, BlogEntry, BlogEntries}
import org.joda.time.DateTime
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}
import slick.driver.H2Driver.api._
import slick.jdbc.meta._
import scala.xml.XML

class BlogTableSuite extends FunSuite with BeforeAndAfter with ScalaFutures {
  implicit override val patienceConfig = PatienceConfig(timeout = Span(5, Seconds))

  val blogEntries = TableQuery[BlogEntries]

  var db: Database = _

  var dateTime = new DateTime(2012, 12, 4, 0, 0, 0, 0)

  def createSchema() = db.run(blogEntries.schema.create).futureValue
  
  def insertBlogEntry(): Int = {
    db.run(blogEntries += BlogEntry(1, dateTime, "Blog Title", "Blog Body", "tag1 tag2")).futureValue
  }
  
  before { db = Database.forConfig("h2mem1") }
  
  test("Creating the Schema works") {
    createSchema()
    
    val tables = db.run(MTable.getTables).futureValue

    assert(tables.size == 1)
    assert(tables.count(_.name.name.equalsIgnoreCase("blogentries")) == 1)
  }

  test("Inserting a Blog works") {
    createSchema()
    
    val insertCount = insertBlogEntry()
    assert(insertCount == 1)
  }
  
  test("Query BlogEntries works") {
    createSchema()
    insertBlogEntry()
    val results = db.run(blogEntries.result).futureValue
    assert(results.size == 1)
    assert(results.head.id == 1)
    assert(results.head.title == "Blog Title")
    assert(results.head.body == "Blog Body")
    assert(results.head.category == "tag1 tag2")
    assert(results.head.pubDate == dateTime)
  }

  test("Create from RSS Blog Entry from File") {
    val blogEntryXml = XML.load(getClass.getResource("/blog-entry.xml").openStream())
    val blogEntries : Seq[BlogEntry] = RssReader.getBlogEntries(blogEntryXml)
    assert(blogEntries.size == 1)
    assert(blogEntries.head.title == "Creme Brulee or Creme Catalan?")
    assert(blogEntries.head.category ==
      "events amsterdam gig tickets google chrome mac web browser development beta")
  }

  test("Create from RSS Blog Entry from RSS URL") {
    val blogEntries : Seq[BlogEntry] = RssReader.extractRss("http://feeds.feedburner.com/OliverCarrsBlog?fmt=xml")
    assert(blogEntries.size >= 4)
    assert(blogEntries.head.title == "Cat Empire Tickets Available Wed 13th Oct at Paradiso Amsterdam")
    assert(blogEntries.takeRight(1).head.title == "'08 hols in Kiel, Copenhagen, and Sweden")
    assert(blogEntries.takeRight(2).head.title == "2fach Kiel, Germany")
    assert(blogEntries.takeRight(3).head.title == "Google Chrome for the Mac: Early Access is Available!")
  }

  after { db.close() }
}
