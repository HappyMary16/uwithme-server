package simulations

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import io.gatling.http.request.builder.HttpRequestBuilder.toActionBuilder

class TestSimulation extends Simulation {

  before {
    println("***** My simulation is about to begin! *****")
  }

  after {
    println("***** My simulation has ended! ******")
  }

  val keycloakAuth: HttpProtocolBuilder = http
    .baseUrl("https://auth.tcomad.tk")

  val theHttpProtocolBuilder: HttpProtocolBuilder = http
    .baseUrl("http://localhost:8081")


  val theScenarioBuilder: ScenarioBuilder = scenario("Scenario1")
    .exec(
      http("keycloakAuth")
        .post("https://auth.tcomad.tk/auth/realms/test/protocol/openid-connect/token")
        .formParam("grant_type", "password")
        .formParam("client_id", "EducationApp-UI")
        .formParam("username", "test-user-1")
        .formParam("password", "test-user-1")
        .check(jsonPath("$.access_token").saveAs("auth"))
      //        .check(regex("access_token: (.*)").find.saveAs("auth"))
    ).exec(
    http("getUniversities")
      .get("http://localhost:8081/api/info/universities")
  ).exec(
    http("getInstitutes")
      .get("http://localhost:8081/api/info/institutes/1")
  ).exec(
    http("getDepartments")
      .get("http://localhost:8081/api/info/departments/1")
  ).exec(
    http("getGroups")
      .get("http://localhost:8081/api/info/groups/1")
  ).exec(
    http("studentRegistration")
      .post("http://localhost:8081/api/auth/signUp")
      .header("Authorization", "Bearer ${auth}")
      .body(StringBody("""{ "role": "1", "instituteId": "1", "universityId": "1", "departmentId": "1", "groupId": "1" }"""))
      .asJson
  ).exec(
    http("deleteUser")
      .delete("http://localhost:8081/api/users")
      .header("Authorization", "Bearer ${auth}")
      .asJson
  )

  setUp(
    theScenarioBuilder.inject(atOnceUsers(1))
  ) //.protocols(theHttpProtocolBuilder)
}