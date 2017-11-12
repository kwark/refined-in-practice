import eu.timepit.refined.auto._
import MyRefinedSlickProfile.api._
import RefinedSchema._
import Schema.developers
import improved_refinements._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}
import org.scalatest.{BeforeAndAfterAll, FunSuite, Matchers}

class RefinedSlickTest extends FunSuite with BeforeAndAfterAll with ScalaFutures with Matchers {

  var db: Database = _

  test("store and retrieve") {
    val developer = Developer("Peter Mortier", "@kwarkk")

    db.run(developers += developer).futureValue
    db.run(developers.length.result).futureValue shouldBe 1

    // slick query
    db.run(developers.filter(_.twitter.like("%kwa%")).result).futureValue should contain theSameElementsAs Seq(developer)

    // plain sql
    import _root_.be.venneborg.refined.RefinedPlainSql._
    db.run(sql"""select name from mytable where twitter like "%rk"""".as[Name]).futureValue should contain theSameElementsAs Seq(developer)

  }

  override def beforeAll() = {
    db = Database.forURL("jdbc:h2:mem:test-refined;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
    db.run(developers.schema.create).futureValue
  }

  override def afterAll(): Unit = {
    db.run(developers.schema.drop).futureValue
    db.close()
  }

  override implicit val patienceConfig= PatienceConfig(Span(2, Seconds))

}
