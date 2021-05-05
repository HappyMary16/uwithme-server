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

  val theHttpProtocolBuilder: HttpProtocolBuilder = http
    .baseUrl("http://localhost:8081")


  val theScenarioBuilder: ScenarioBuilder = scenario("Scenario1")
    .exec(
      http("getUniversities")
        .get("/api/info/universities")
    )

  setUp(
    theScenarioBuilder.inject(atOnceUsers(1))
  ).protocols(theHttpProtocolBuilder)
}