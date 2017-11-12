import be.venneborg.refined.{RefinedMapping, RefinedSupport}
import slick.jdbc.H2Profile

trait MyRefinedSlickProfile extends H2Profile
  with RefinedMapping
  with RefinedSupport {

  override val api = new API with RefinedImplicits

}

object MyRefinedSlickProfile extends MyRefinedSlickProfile
