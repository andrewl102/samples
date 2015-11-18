name := """ebean-sample"""

version := "1.0-SNAPSHOT"
//logLevel := Level.Debug

lazy val root = (project in file(".")).enablePlugins(PlayJava,PlayEbean)

scalaVersion := "2.10.2"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "org.avaje.ebeanorm" % "avaje-ebeanorm" % "6.7.1",
  "org.avaje.ebeanorm" % "avaje-ebeanorm-typequery" % "1.5.1",
  "org.avaje.ebeanorm" % "avaje-ebeanorm-typequery-generator" % "1.5.1",
  "org.avaje.ebeanorm" % "avaje-ebeanorm-typequery-agent" % "1.5.1",
  "org.postgresql" % "postgresql" % "9.4-1202-jdbc41"
)

libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"
libraryDependencies += "junit" % "junit" % "4.11" % "test"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

playEnhancerEnabled := false
playEbeanModels in Compile := Seq("models.*")
playEbeanDebugLevel := 9

playEbeanQueryGenerate := true
playEbeanQueryEnhance := true