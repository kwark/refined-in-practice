import eu.timepit.refined.api.Refined
import eu.timepit.refined.boolean.{AllOf, And, Not, Or}
import eu.timepit.refined.string.{MatchesRegex, StartsWith}
import eu.timepit.refined.W
import eu.timepit.refined.char.LetterOrDigit
import eu.timepit.refined.collection.{MaxSize, NonEmpty, Tail}
import eu.timepit.refined.generic.Equal
import shapeless.::
import shapeless.HNil

object improved_refinements {

  type TwitterHandle = String Refined AllOf[
    StartsWith[W.`"@"`.T] :: MaxSize[W.`16`.T] ::
    Not[MatchesRegex[W.`"(?i:.*twitter.*)"`.T]] ::
    Not[MatchesRegex[W.`"(?i:.*admin.*)"`.T]] ::
    Tail[Or[LetterOrDigit, Equal[W.`'_'`.T]]] ::
    HNil
  ]

  type Name = String Refined And[NonEmpty, MaxSize[W.`256`.T]]

  final case class Developer(name: Name, twitterHandle: TwitterHandle)

}

