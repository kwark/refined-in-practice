import improved_refinements.{Developer, Name, TwitterHandle}

object RefinedSchema {

  import MyRefinedSlickProfile.api._
  import MyRefinedSlickProfile.mapping._

  class Developers(tag: Tag) extends Table[Developer](tag, "developers") {
    def twitter = column[TwitterHandle]("twitter", O.PrimaryKey)
    def name    = column[Name]("name")

    def * = (name, twitter).mapTo[Developer]
  }

  val developers = TableQuery[Developers]

}
