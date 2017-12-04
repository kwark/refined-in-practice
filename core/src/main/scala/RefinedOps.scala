import eu.timepit.refined.api.{RefType, Refined}
import eu.timepit.refined.types.numeric._
import eu.timepit.refined.types.string._

object RefinedOps {

  implicit class NonEmptyStringOps(val s: NonEmptyString) {

    def append(o: NonEmptyString): NonEmptyString = Refined.unsafeApply(s.value + o.value)

  }

  implicit class PosIntOps(val p: PosInt) {

    def minus(that: PosInt): Option[PosInt] = RefType.applyRef[PosInt](p.value - that.value).toOption

    def add(that: PosInt): Option[PosInt] = RefType.applyRef[PosInt](p.value + that.value).toOption

    def unsafeAdd(that: PosInt): PosInt = Refined.unsafeApply(p.value + that.value)

  }


}
