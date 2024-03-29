package simulations

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder.toActionBuilder

import scala.concurrent.duration.DurationInt

class StudentCreationSimulation extends Simulation {

  val uWithMeService = "http://localhost:8081"
  val keycloakService = "https://auth.tcomad.tk/auth"

  val feeder = csv("generate.csv").circular

  val theScenarioBuilder: ScenarioBuilder = scenario("StudentCreationScenario")
    .feed(feeder)
    .exec(
      http("keycloakAuth")
        .post(keycloakService + "/auth/realms/test/protocol/openid-connect/token")
        .formParam("grant_type", "password")
        .formParam("client_id", "UwithmeServiceApp-UI")
        .formParam("username", "${username}")
        .formParam("password", "${password}")
        .check(jsonPath("$.access_token").saveAs("token"))
    ).exec(
    http("getUniversities")
      .get(uWithMeService + "/api/info/universities")
  ).exec(
    http("getInstitutes")
      .get(uWithMeService + "/api/info/institutes/1")
  ).exec(
    http("getDepartments")
      .get(uWithMeService + "/api/info/departments/1")
  ).exec(
    http("getGroups")
      .get(uWithMeService + "/api/info/groups/1")
  ).exec(
    http("studentRegistration")
      .post(uWithMeService + "/api/auth/signUp")
      .header("Authorization", "Bearer ${token}")
      .body(StringBody("""{ "role": "1", "instituteId": "1", "universityId": "1", "departmentId": "1" }"""))
      .asJson
  ).exec(
    http("deleteUser")
      .delete(uWithMeService + "/api/users")
      .header("Authorization", "Bearer ${token}")
      .asJson
  )

  setUp(
    theScenarioBuilder.inject(rampConcurrentUsers(1) to (50) during (1.minutes),
      constantConcurrentUsers(500) during (1.minutes),
      rampConcurrentUsers(500) to (1) during (1.minutes))
  ) //.protocols(theHttpProtocolBuilder)
}