package user

import io.gatling.core.Predef._ 
import io.gatling.http.Predef._
import scala.concurrent.duration._

class user extends Simulation { 
  val jwt = "[secret JWT]"
  val baseurl = "http://api-citrakara.herokuapp.com/v1"

  val usernameField = "username"
  val emailField = "email"
  val bioField = "bio"

  val header_1 =  Map(
      "Content-Type" -> "application/json",
      "Accept-Charset" -> "utf-8",
      "Authorization" -> jwt )

  val httpConf = http 
    .baseURL(baseurl) 
    .headers(header_1)

  val scn1 = scenario("Testing users Endpoints")
    .exec(http("get all users")
      .get("/user/all")
      .check(status.is(200))
      .check(jsonPath("$..username"))
      .check(jsonPath("$..email"))
      )

    .exec(http("get current user")
      .get("/user")
      .check(status.is(200))
      .check(jsonPath("$..username"))
      .check(jsonPath("$..email"))
      ) 
   
    .exec(http("edit current user")
      .put("/user/edit")
      .body(StringBody("""{ "username":"updated", 
                            "bio":"updated bio",
                            "telp":"updated telp" }""")).asJSON
      .check(status.is(201))
      .check(jsonPath("$..username"))
      .check(jsonPath("$..email"))      
      )

  val scn2 = scenario("Testing paintings endpoints")
    .exec(http("Get paintings")
      .get("/paintings")
      .check(status.is(200))
      .check(jsonPath("$..title"))
      .check(jsonPath("$..description")) 
    )


  setUp(scn1.inject(atOnceUsers(1)), scn2.inject(atOnceUsers(1)))
    .protocols(httpConf)
}
