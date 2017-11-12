import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.string.StartsWith


object refinements {
  type Name = String Refined NonEmpty
  type TwitterHandle = String Refined StartsWith[W.`"@"`.T]

  final case class Developer(name: Name, twitterHandle: TwitterHandle)
}

