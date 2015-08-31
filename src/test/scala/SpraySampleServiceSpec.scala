import com.mlh.spraysample.Boot
import com.mlh.spraysample.basic.SpraySampleService
import org.scalatest.FlatSpec
import spray.testkit.ScalatestRouteTest
import spray.http.StatusCodes

/**
 * Integration test for the {@link SpraySampleService}
 */
class SpraySampleServiceSpec extends FlatSpec with ScalatestRouteTest with SpraySampleService {

  val boot = Boot
  def actorRefFactory = boot.system

  "Boot" should "not have null fields" in {
    assert(boot.service === null)
    assert(boot.system === null)
  }

  "Entity" should "result" in {
    Get("/entity") ~> spraysampleRoute ~> check {

      assertResult(StatusCodes.OK) {
        status
      }

      assertResult("[{\"bar\":\"foo1\"},{\"bar\":\"foo2\"}]") {
        responseAs[String]
      }
    }

  }
}
