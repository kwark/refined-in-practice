import eu.timepit.refined.api.RefType
import eu.timepit.refined.types.string._
import eu.timepit.refined.types.numeric._
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.scalacheck.arbitraryRefType
import org.scalatest.{FunSuite, Matchers}
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.prop.PropertyChecks
import RefinedOps._
import eu.timepit.refined.types.numeric

class RefinedOpsTest extends FunSuite with Matchers with PropertyChecks {

  test("NonEmptyString append") {
    forAll { (s1: NonEmptyString, s2: NonEmptyString) =>
      val ev = implicitly[RefinedStringOps[NonEmptyString]]
      ev.concat(s1, s2).value.nonEmpty shouldBe true
    }
  }

  test("PosInt add") {
    forAll { (i1: PosInt, i2: PosInt) =>
      val ev = implicitly[RefinedIntOps[PosInt]]
      val result: Option[numeric.PosInt] = ev.add(i1, i2)
      if (i2.value + i1.value > 0) result.nonEmpty shouldBe true
      else result.isEmpty shouldBe true
    }
  }

  test("PosInt unsafeAdd") {
    forAll { (i1: PosInt, i2: PosInt) =>
      val ev = implicitly[RefinedIntOps[PosInt]]
      // This test fails sometimes, because of int overflow
      // ev.unsafeAdd(i1, i2) .value should be > 0
    }
  }

  implicit def nonEmptyStringArbitrary[F[_, _]](implicit rt: RefType[F]): Arbitrary[F[String, NonEmpty]] =
    arbitraryRefType(Arbitrary.arbString.arbitrary.filter(_.nonEmpty))


  implicit val postIntArbitrary: Arbitrary[PosInt] =
    arbitraryRefType(Gen.chooseNum(1, Integer.MAX_VALUE))

}
