import eu.timepit.refined.auto._
import eu.timepit.refined.api.RefType
import improved_refinements._
import org.scalatest.{FunSuite, Matchers}
import play.api.data.{Form, FormError, Forms}
import play.api.data.Forms.mapping
import play.api.data.format.Formatter

class FormTest extends FunSuite with Matchers {

  val developer = Developer("Peter Mortier", "@kwarkk")

  test("manual form binding") {

    implicit val twitterHandleFormatter = new Formatter[TwitterHandle] {

      override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], TwitterHandle] = {
        data.get(key) match {
          case None    => Left(Seq(FormError(key, "error.required")))
          case Some(v) => RefType.applyRef[TwitterHandle](v) match {
            case Right(v)     => Right(v)
            case Left(error)  => Left(Seq(FormError(key, error)))
          }
        }

      }

      override def unbind(key: String, value: TwitterHandle) = Map[String, String](key -> value.value)

    }

    implicit val nameFormatter = new Formatter[Name] {

      override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], Name] = {
        data.get(key) match {
          case None    => Left(Seq(FormError(key, "error.required")))
          case Some(v) => RefType.applyRef[Name](v) match {
            case Right(v)     => Right(v)
            case Left(error)  => Left(Seq(FormError(key, error)))
          }
        }

      }

      override def unbind(key: String, value: Name) = Map[String, String](key -> value.value)

    }

    val developerForm = Form(mapping(
        "name" -> Forms.of[Name],
        "handle" -> Forms.of[TwitterHandle]
      )(Developer.apply)(Developer.unapply)
    )

    developerForm.bind(
      Map("name" -> "Peter Mortier", "handle" -> "@kwarkk")
    ).value shouldBe Some(developer)

    developerForm.bind(
      Map("name" -> "Peter Mortier", "handle" -> "kwarkk")
    ).value shouldBe None
  }

  test("auto form binding") {

    import _root_.be.venneborg.refined.play.RefinedForms._

    val developerForm = Form(mapping(
      "name" -> Forms.of[Name],
      "handle" -> Forms.of[TwitterHandle]
    )(Developer.apply)(Developer.unapply)
    )

    developerForm.bind(
      Map("name" -> "Peter Mortier", "handle" -> "@kwarkk")
    ).value shouldBe Some(developer)

    developerForm.bind(
      Map("name" -> "Peter Mortier", "handle" -> "kwarkk")
    ).value shouldBe None
  }

}
