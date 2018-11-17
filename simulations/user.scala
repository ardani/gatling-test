package user

import io.gatling.core.Predef._ 
import io.gatling.http.Predef._
import scala.concurrent.duration._

class user extends Simulation { 
  val jwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1NzQwMDYwMjUsInN1YiI6MjV9.BRw4LVstlPjl6nyQV_6Yx7CA9tN9GakX7Ia2hk9HLVQ"
  val baseurl = "http://api-citrakara.herokuapp.com/v1"

  val usernameField = "username"
  val emailField = "email"
  val bioField = "bio"

  val header =  Map(
      "Content-Type" -> "application/json",
      "Accept-Charset" -> "utf-8",
      "Authorization" -> jwt )

  val httpConf = http 
    .baseURL(baseurl) 
    .headers(header)

  val scn = scenario("Testing users Endpoints")
    .exec(http("User Sign up")
      .post("/user/signup")
      .body(StringBody("""{ "user":
                             {
                              "username":"artistku", 
                              "email":"artistku@email.com",
                              "password":"123456", 
                              "password_confirmation":"123456",
                              "artist":"1"
                              }
                          }""")).asJSON
      .check(status.is(201))
      .check(jsonPath("$..password_digest"))
    )
        
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

  setUp(scn.inject(atOnceUsers(1)))
      .protocols(httpConf)
}
