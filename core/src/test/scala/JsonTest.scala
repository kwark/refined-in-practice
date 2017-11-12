import eu.timepit.refined.api.RefType
import eu.timepit.refined.auto._
import improved_refinements._
import org.scalatest.{FunSuite, Matchers}
import play.api.libs.json._

class JsonTest extends FunSuite with Matchers {

  val developer = Developer("Peter Mortier", "@kwarkk")

  test("no json format") {
    """implicit val developerFormat = Json.format[Developer]""" shouldNot compile
  }

  test("manual json format") {

    implicit val twitterHandleFormat = new Format[TwitterHandle] {

      override def reads(json: JsValue) = json.validate[String] flatMap { s =>
        RefType.applyRef[TwitterHandle](s) match {
          case Right(handle) => JsSuccess(handle)
          case Left(err)     => JsError(err)
        }
      }

      override def writes(handle: TwitterHandle) = JsString(handle.value)

    }

    implicit val nameFormat = new Format[Name] {

      override def reads(json: JsValue) = json.validate[String] flatMap { s =>
        RefType.applyRef[Name](s) match {
          case Right(name) => JsSuccess(name)
          case Left(err)   => JsError(err)
        }
      }

      override def writes(name: Name) = JsString(name.value)
    }

    implicit val developerFormat = Json.format[Developer]

    val result: JsValue = Json.toJson(developer)
    Json.fromJson[Developer](result).get shouldBe developer

  }


}
