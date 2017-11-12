import java.sql.SQLException

import eu.timepit.refined.api.{RefType, Refined}
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

  class Developers(tag: Tag) extends Table[Developer](tag, "developers") {
    def twitter = column[TwitterHandle]("twitter", O.PrimaryKey)
    def name    = column[Name]("name")

    def * = (name, twitter) <> (Developer.tupled, Developer.unapply)
  }

  val developers = TableQuery[Developers]


}
