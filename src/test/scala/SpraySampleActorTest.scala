import akka.actor.ActorSystem
import akka.testkit.{TestKit, TestActorRef}
import com.mlh.spraysample.SpraySampleActor


class SpraySampleActorTest extends TestKit(ActorSystem("testSystem"))
  with org.scalatest.WordSpecLike with org.scalatest.MustMatchers {

  "A spray actor" must {
    // Creation of the TestActorRef
    val actorRef = TestActorRef[SpraySampleActor]

    "receive messages" in {

      actorRef ! "word!"
    }
  }

}
