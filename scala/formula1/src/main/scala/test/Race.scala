package test


/**
 * Assumptions:
 * 1) We assume the track can extend infinitely backwards to enable a large number of cars to be i * 150 metres from the first car
 * 2) All cars are infinitesimally small, and are at a horizontal distance that is also infinitesimally small.
 *    This means we can calculate the distance between cars using one dimension only and we don't have to worry about
 *    issues with them blocking each other etc.
 * 3) The cars can brake instantly to support the handling factor change
 * 4) Despite point 3, cars will continue to accelerate regardless of handling factor or not. This is unclear from the
 *    problem description whether this is desired or not
 * 5) Drivers do not want to totally waste Nitro, so they will not use it if they have a car acceleration of 0
 * 6) Cars continue driving past the finish line and this does not affect there decision process
 * 7) Cars acceleration is completely linear until it reaches maximum speed at which point it is becomes 0
 * 8) We limit the n to a reasonable value for scalability purposes and to avoid potential overflow
 * 9) We don't care about rounding errors and the use of floating point numbers
 * 10) We don't want to handle empty cars or a track of length less than 1
 * 11) Nitro will not be used when there is only one car in the race
 * 12) We don't care about wasting Nitro when we are at max speed
 */

object Race {
  val NitroFactor = 2

  val HandlingFactor = 0.8
  val BaseTopSpeed = 150
  val TopSpeedMultiplier = 10
  val PositionMultiplier = 200

  val Acceleration = 2
  val CheckInterval = 2
  val DistanceCheck = 10

  val MaxCars = 100000

  def build(n:Int, length:Double):Race = {
    assert(n >0 )
    assert(n <= MaxCars )
    assert(length >= 1)
    val teams = (1 to n).map(i => new Team(i)).toList
    Race(teams, Track(length))
  }

  def buildFrom(cars:List[Car],length:Double):Race = {
    assert(cars.nonEmpty)
    assert(cars.size <= MaxCars)
    assert(length >=1)
    Race(cars.zipWithIndex.map(withIndex => Team(withIndex._2,withIndex._1)),Track(length))
  }

  /*
    Solve a quadratic equation for distance.
   */
  def solveForDistance(acc: Double, vel: Double, distance: Double): Double = {
    if(acc == 0d) distance / vel else {
      val temp1: Double = Math.sqrt(vel * vel + 4 * acc * distance)
      val root1: Double = (-vel + temp1) / (2 * acc)
      val root2: Double = (-vel - temp1) / (2 * acc)
      if (root1 >= 0) root1 else root2
    }
  }
  def kphToMetresPerSecond(kph:Double):Double = kph * 100 / 360
}

case class Race(teams: List[Team], track: Track) {
  def run(checkInterval:Int = Race.CheckInterval): Unit = {
    while(teams.exists(t => t.car.carState.finished.isEmpty)) {
      val allCars = teams.map(_.car)
      allCars.foreach(_.updateState(checkInterval, allCars, track))
    }
  }
}

/**
 *  This class is not really useful outside of modelling purposes.
 */
case class Team(n:Int, car: Car) { def this(n:Int) = this(n,Car.build(n)) }

case class Car(maxVelocity: Double, acceleration: Double, carState: CarState) {
  val driver = Driver()

  /**
   * We have no real interest in this class for the purpose of the challenge.
   * It is included simply for modelling purposes or to support extension at a later date.
   * The behaviour encapsulates what the driver logically does as opposed to what the car does.
   */
  case class Driver() {
    def performChecks(allCars:List[Car]): Unit = {
      //We could optimise this so that the calculation could be done in O(n) but this would negatively impact
      //the clarity / OO design so it simply uses an inefficient O(n^2) solution. It could also be further
      //optimised for efficiency of the checks or moving the filtering outside this method
      val others = allCars.toBuffer - Car.this
      val isLast = others.forall(c => c.carState.position > carState.position)
      val carNearby = others.exists(c =>  Math.abs(carState.position - c.carState.position) <= Race.DistanceCheck)

      /* Decision process is as follows:
        1) Using the Nitro is considered higher priority than the handling factor
        2) Don't use Nitro if you are not moving, it will have no effect as 2 * 0 is still 0
        3) Reduce the handling speed if condition 1 and 2 were not met and there is a car nearby
        4) Continue as normal
      */
      val canUseNitro = !carState.usedNitro && carState.speed > 0 && others.nonEmpty
      if(canUseNitro && isLast) {
        activateNitro()
      } else if(carNearby) {
        activateHandlingFactor()
      }
    }
  }

  def updateState(seconds:Int, allCars:List[Car], track:Track = Track(Double.MaxValue)) = {
    driver.performChecks(allCars)
    updatePosition(seconds, track)
  }

  def updatePosition(seconds:Int, track:Track = Track(Double.MaxValue)):Unit = {
    val carState = this.carState
    def increment(time:Double,acc:Double) = {
      val positionInTime: Double = carState.position + (carState.speed * time) + (acc * time * time) / 2

      if(positionInTime >= track.trackLength && carState.finalSpeed.isEmpty) {
        //See if we have hit the finish line during execution
        val remaining = track.trackLength - carState.position
        val finishedAt = Race.solveForDistance(acc, carState.speed, remaining)
        carState.finished = Some(carState.seconds + finishedAt)
        carState.finalSpeed = Some(carState.speed + finishedAt * acc)
      }
      carState.position = positionInTime
      carState.speed = carState.speed + (acc * time)
      carState.seconds = carState.seconds + time
    }
    //We could hit maximum velocity
    val timeAccelerating = Math.min(seconds, (maxVelocity - carState.speed) / acceleration)
    increment(timeAccelerating, acceleration)
    increment(seconds - timeAccelerating, 0)
  }

  def activateNitro(): Unit = {
    assert(!carState.usedNitro)
    carState.speed = Math.min(carState.speed * Race.NitroFactor, maxVelocity)
    carState.usedNitro = true
  }

  def activateHandlingFactor(): Unit = {
    carState.speed = carState.speed * Race.HandlingFactor
  }
}

object Car {
  def build(n: Int, base:Double = Race.BaseTopSpeed, multiple:Double = Race.TopSpeedMultiplier, acceleration: Int = Race.Acceleration): Car = {
    assert(n > 0)
    Car( Race.kphToMetresPerSecond(base + multiple * n),acceleration * n, new CarState(0d, 0d, (n-1) * -1 * Race.PositionMultiplier) )
  }
}

case class CarState(  var seconds:Double, var speed: Double, var position: Double, var usedNitro:Boolean=false,
                      var finished:Option[Double] = None,var finalSpeed:Option[Double] = None)

/**
 * For the purposes of this exercise is not really necessary to include a definition for the track,
 * however if the problem were more complex / the solution needed to be extended in the future it would be a wise
 * idea.
 * @param trackLength The length of the track, in metres
 */
case class Track(trackLength: Double)
