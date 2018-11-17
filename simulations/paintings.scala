package paintings

import io.gatling.core.Predef._ 
import io.gatling.http.Predef._
import scala.concurrent.duration._

class user extends Simulation { 
  val jwt = "[secret JWT]"
  val baseurl = "http://api-citrakara.herokuapp.com/v1"
  val paintingId = 1
  val header =  Map(
      "Content-Type" -> "application/json",
      "Accept-Charset" -> "utf-8",
      "Authorization" -> jwt )

  val httpConf = http 
    .baseURL(baseurl) 
    .headers(header)

  val scn = scenario("Testing paintings endpoints")
    .exec(http("Get paintings")
      .get("/paintings")
      .check(status.is(200))
      .check(jsonPath("$..title"))
      .check(jsonPath("$..description")) 
    )
    .exec(http("Post a painting")
      .post("/paintings")
      .body(StringBody("""{ 
                      "title":"automated test", 
                      "description":"gatling automated test"
                          }""")).asJSON
    )


  setUp(scn.inject(atOnceUsers(1)))
    .protocols(httpConf)
}