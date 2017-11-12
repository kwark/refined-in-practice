import eu.timepit.refined.api.RefType
import improved_refinements._
import org.scalatest.{FunSuite, Matchers}

class ImprovedTest extends FunSuite with Matchers {

  test("improved TwitterHandle") {

    RefType.applyRef[TwitterHandle]("@kwarkk").isRight shouldBe true

    RefType.applyRef[TwitterHandle]("kwarkk").isRight shouldBe false

    RefType.applyRef[TwitterHandle]("@tooooolooooongggggg").isRight shouldBe false

    RefType.applyRef[TwitterHandle]("@_woot_").isRight shouldBe true

    RefType.applyRef[TwitterHandle]("@not-ok").isRight shouldBe false

    RefType.applyRef[TwitterHandle]("@not$ok").isRight shouldBe false

    RefType.applyRef[TwitterHandle]("@@foo").isRight shouldBe false

    RefType.applyRef[TwitterHandle]("@my_twitter_").isRight shouldBe false

    RefType.applyRef[TwitterHandle]("@AdMinsRule").isRight shouldBe false

  }


}
