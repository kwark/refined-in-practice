import eu.timepit.refined.auto._
import org.scalatest.{BeforeAndAfterAll, FunSuite, Matchers}
import slick.jdbc.H2Profile.api._
import improved_refinements._
import Schema._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}

class SlickTest extends FunSuite with BeforeAndAfterAll with ScalaFutures with Matchers {

  var db: Database = _

  test("store") {
    val developer = Developer("Peter Mortier", "@kwarkk")

    db.run(developers += developer).futureValue
    db.run(developers.length.result).futureValue shouldBe 1
    """developers.filter(_.twitter.like("kwa%"))""" shouldNot compile
    db.run(developers.filter(_.twitter.asInstanceOf[Rep[String]].like("%kwa%")).result).futureValue should contain theSameElementsAs Seq(developer)
  }


  override def beforeAll() = {
    db = Database.forURL("jdbc:h2:mem:test-nonrefined;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
    db.run(developers.schema.create).futureValue
  }

  override def afterAll(): Unit = {
    db.run(developers.schema.drop).futureValue
    db.close()
  }

  override implicit val patienceConfig= PatienceConfig(Span(2, Seconds))

}
