import java.sql.SQLException

import eu.timepit.refined.api.{RefType, Refined}
import eu.timepit.refined.types.numeric.PosInt
import improved_refinements.{Developer, Name, TwitterHandle}


object Schema {

  import slick.jdbc.H2Profile.api._

  implicit val twitterHandleMapping = MappedColumnType.base[TwitterHandle, String](
    _.value, Refined.unsafeApply //Hmm, if database value is incorrect, we have a misbehaving Refined type
  )

  implicit val nameMapping = MappedColumnType.base[Name, String](
    _.value, v => RefType.applyRef[Name](v) match { // This is much better, we refine our type at runtime
      case Right(rt) => rt
      case Left(e)   => throw new SQLException(s"can not refine '$v'", e) // and throw an SQLException when it does not satisfy the predicate
    }
  )

  class Developers(tag: Tag) extends Table[Developer](tag, "DEVELOPERS") {
    def twitter = column[TwitterHandle]("TWITTER", O.PrimaryKey)
    def name    = column[Name]("NAME")

    def * = (name, twitter) <> (Developer.tupled, Developer.unapply)
  }

  implicit val postIntMapping = MappedColumnType.base[PosInt, Int](
    _.value, Refined.unsafeApply
  )

  class TestTable(tag: Tag) extends Table[(PosInt)](tag, "TESTING") {
    def number = column[PosInt]("NUMBER", O.PrimaryKey)

    def * = (number)
  }

  val developers = TableQuery[Developers]
  val testTable = TableQuery[TestTable]


}
