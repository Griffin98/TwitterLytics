name := """TwitterLytics-v2"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.1"


libraryDependencies += guice
libraryDependencies += ws
libraryDependencies += "javax.xml.bind" % "jaxb-api" % "2.1"

// Test Database
libraryDependencies += "com.h2database" % "h2" % "1.4.199"

// Testing libraries for dealing with CompletionStage...
libraryDependencies += "org.assertj" % "assertj-core" % "3.14.0" % Test
libraryDependencies += "org.awaitility" % "awaitility" % "4.0.1" % Test
libraryDependencies += "junit" % "junit" % "4.12" % "test"
libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"


libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.6.10" % Test
libraryDependencies += "com.typesafe.akka" %% "akka-slf4j" % "2.6.10" % Test
libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % "2.6.10" % Test
libraryDependencies += "com.typesafe.akka" %% "akka-protobuf-v3" % "2.6.10" % Test
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.6.10" % Test
libraryDependencies += "com.typesafe.akka" %% "akka-serialization-jackson" % "2.6.10" % Test

libraryDependencies ++= Seq(
  "org.twitter4j" % "twitter4j-core" % "4.0.4",
)

// Make verbose tests
testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
