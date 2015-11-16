package test

import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class RaceSpec extends FlatSpec with Matchers {
  //Below use values which result in no rounding
  def makeFastCar = Car.build(1,14.3,0.1)
  def makeSlowerCar = Car.build(1,10.7,0.1)


  "Quadratic formula should " should "work" in {
    val a: Int = 2
    val b: Int = 0
    val c: Int = 18
    Race.solveForDistance(a,b,c) shouldEqual 3
  }

  "A single car" should "start with everything zeroed" in {
    val car = makeFastCar
    car.carState.position shouldEqual 0
    car.carState.speed shouldEqual 0
  }
  it should "remain zero after zero seconds" in {
    val car = makeFastCar
    car.updatePosition(0)
    car.carState.position shouldEqual 0
    car.carState.speed shouldEqual 0
  }

  it should "have the velocity = acceleration and position = acceleration / 2 after 1 second" in {
    val car = makeFastCar
    car.updatePosition(1)
    car.carState.position shouldEqual 1
    car.carState.speed shouldEqual 2
  }

  it should "have the velocity = acceleration * 2 and position = acceleration * 2 after 2 second" in {
    val car = makeFastCar
    car.updatePosition(2)
    car.carState.position shouldEqual 4
    car.carState.speed shouldEqual 4
  }

  it should "calculate the final position when max acceleration hit exactly and have the velocity = acceleration * 2 " +
    "and position = acceleration * 2 after 2 second in two calculations" in {
    val car = makeFastCar
    car.updatePosition(1)
    car.updatePosition(1)
    car.carState.position shouldEqual 4
    car.carState.speed shouldEqual 4
  }

  it should "calculate position properly when max velocity is hit" in {
    val car = makeSlowerCar
    car.updatePosition(1)
    car.carState.position shouldEqual 1
    car.carState.speed shouldEqual 2
    car.updatePosition(1)
    car.carState.speed shouldEqual 3

    //2 + (1 + 0.5) + (0.5 * 3 + 0)
    car.carState.position shouldEqual 3.75
    car.updatePosition(1)
    car.carState.position shouldEqual 6.75
  }

  it should "calculate the final time correctly when it has finished accelerating" in {
    val car = makeSlowerCar
    car.updatePosition(4)
    car.carState.position shouldEqual 9.75 //As above, but with 1 * 3 added
    //This reflects a check which happened at 5 seconds, by which time we have crossed it
    //In this scenario, we should have max speed = 3, and time = 0.75 / 3 = 4 + 0.25 seconds
    car.updatePosition(1,Track(10.5))
    car.carState.finalSpeed.get shouldEqual 3
    car.carState.finished.get shouldEqual 4.25
  }

  it should "calculate the final time correctly when it is still accelerating" in {
    val car = makeFastCar
    car.updatePosition(1)
    //Should hit the track line at 3 metres while it is still accelerating
    car.updatePosition(1,Track(3))
    car.carState.finalSpeed.get should  be (3.2 +- 0.1)
    car.carState.finished.get should be (1.61 +- 0.1)
  }

  it should "use nitro properly " in {
    val car = makeFastCar
    val otherCar = makeSlowerCar
    otherCar.carState.position = 1000d //Put it ahead
    //Use another car to force the check when the speed is zero
    car.driver.performChecks(List(otherCar))
    car.carState.usedNitro shouldBe false

    car.updatePosition(1)
    //Now we're moving. Clone and check it doesn't nitro when no-one else
    val testState = car.copy()
    testState.driver.performChecks(Nil)
    testState.carState.usedNitro shouldBe false

    car.driver.performChecks(List(otherCar))
    car.carState.usedNitro shouldBe true
    car.carState.speed shouldBe 4
  }

  "the race" should "check positions properly" in {
    val car = makeFastCar
    car.carState.usedNitro = true
    car.carState.speed=car.maxVelocity
    val otherCar = makeSlowerCar
    otherCar.carState.speed=otherCar.maxVelocity
    //Far away from each other
    otherCar.carState.position = 1000d
    otherCar.carState.usedNitro = true
    car.driver.performChecks(List(otherCar))
    otherCar.driver.performChecks(List(car))

    otherCar.carState.speed shouldBe otherCar.maxVelocity
    car.carState.speed shouldBe car.maxVelocity

    //Now put them next to each other
    otherCar.carState.position = 0
    car.driver.performChecks(List(otherCar))
    otherCar.driver.performChecks(List(car))

    otherCar.carState.speed shouldBe 0.8 * otherCar.maxVelocity
    car.carState.speed shouldBe 0.8 * car.maxVelocity
  }

  it should "should work for the whole race as a whole" in {
    //The same assertions as the above final time calculation
    val car = makeSlowerCar
    val race = Race.buildFrom(List(car), 10.5)
    race.run(1)
    car.carState.finalSpeed.get shouldEqual 3
    car.carState.finished.get shouldEqual 4.25
  }

  it should "should work for the whole race as a whole using defaults" in {
    val race = Race.build(2, 1000)
    race.run()
    race.teams.head.car.carState.finalSpeed.get shouldEqual 44.4 +- 0.1
    race.teams.head.car.carState.finished.get shouldEqual 31.2 +- 0.1

    race.teams.last.car.carState.finalSpeed.get shouldEqual 47.2 +- 0.1
    race.teams.last.car.carState.finished.get shouldEqual 29.82 +- 0.1
  }

  it should "should build initial conditions properly" in {
    val race = Race.build(2, 1000)
    race.teams.head.car.carState.position shouldBe 0
    race.teams.last.car.carState.position shouldBe -200

    race.teams.head.car.maxVelocity shouldBe Race.kphToMetresPerSecond(160)
    race.teams.last.car.maxVelocity shouldBe Race.kphToMetresPerSecond(170)

    race.teams.head.car.acceleration shouldBe 2
    race.teams.last.car.acceleration shouldBe 4

  }

  "two cars in a race" should " have the second car overtake and win" in {
    val first = Car.build(1,14.3,0.1)
    //Second one can accelerate to very high speed
    val second = Car.build(2,0,180,100)
    val race = Race.buildFrom(List(first, second), 1000)
    race.run(1)
    first.carState.position shouldBe 1000
    first.carState.finished.get shouldBe 251
    second.carState.position shouldBe 24875.0
    second.carState.finished.get shouldBe 12.25
  }
}
