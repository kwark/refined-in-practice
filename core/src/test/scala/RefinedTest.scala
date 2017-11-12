import eu.timepit.refined.collection.NonEmpty
import org.scalatest.{FunSuite, Matchers}

class RefinedTest extends FunSuite with Matchers {

  test("auto macro") {
    import eu.timepit.refined.auto._

    """refinements.Developer("Peter Mortier", "@kwarkk")""" should compile

    val developer = refinements.Developer("Peter Mortier", "@kwarkk")
    developer.name.value shouldBe "Peter Mortier"
    developer.twitterHandle.value shouldBe "@kwarkk"

    """refinements.Developer("@kwarkk", "Peter Mortier")""" shouldNot compile
  }

  test("manual macro") {
    import eu.timepit.refined.refineMV

    // requires to pass the predicate, which for simple predicates is fine
    """refineMV[NonEmpty]("Peter")""" should compile
    refineMV[NonEmpty]("Peter").value shouldBe "Peter"

    import eu.timepit.refined.api.RefType

    // alternative using the type alias for the refined type
    """RefType.applyRefM[refinements.Name]("Peter")""" should compile
    RefType.applyRefM[refinements.Name]("Peter").value shouldBe "Peter"

    // comes in handy for complex predicates
    """RefType.applyRefM[refinements.TwitterHandle]("@kwarkk")""" should compile
    RefType.applyRefM[refinements.TwitterHandle]("@kwarkk").value shouldBe "@kwarkk"

    // and validates just the same
    """RefType.applyRefM[refinements.TwitterHandle]("foo")""" shouldNot compile

  }

  test("runtime validation") {
    import eu.timepit.refined.refineV

    // requires to pass the predicate, which for simple predicates is fine
    refineV[NonEmpty]("Peter") match {
      case Right(r) => r.value shouldBe "Peter"
      case Left(e)  => fail(e)
    }

    import eu.timepit.refined.api.RefType

    // alternative using the type alias for the refined type
    RefType.applyRef[refinements.Name]("Peter") match {
      case Right(r) => r.value shouldBe "Peter"
      case Left(e)  => fail(e)
    }

    // comes in handy for complex predicates
    RefType.applyRef[refinements.TwitterHandle]("@kwarkk") match {
      case Right(r) => r.value shouldBe "@kwarkk"
      case Left(e)  => fail(e)
    }

    RefType.applyRef[refinements.TwitterHandle]("foo") match {
      case Right(r) => fail("should fail")
      case Left(e)  => succeed
    }


  }



}
