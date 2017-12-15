import eu.timepit.refined.api.{RefType, Refined}
import eu.timepit.refined.types.numeric._
import eu.timepit.refined.types.string._

object RefinedOps {

  trait RefinedStringOps[T] {
    def concat(t1: T, t2: T): T
  }

  implicit val refinedNonEmptyStringOps = new RefinedStringOps[NonEmptyString] {
    override def concat(t1: NonEmptyString, t2: NonEmptyString): NonEmptyString =
      Refined.unsafeApply(t1.value + t2.value)
  }

  trait RefinedIntOps[T] {
    def unsafeAdd(t1: T, t2: T): T
    def add(t1: T, t2: T): Option[T]
  }

  implicit val refinedPosIntOps = new RefinedIntOps[PosInt] {

    override def unsafeAdd(x: PosInt, y: PosInt): PosInt =
      Refined.unsafeApply(x.value + y.value)


    override def add(x: PosInt, y: PosInt): Option[PosInt] =
      RefType.applyRef[PosInt](x.value + y.value).toOption

  }


}
