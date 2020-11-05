lazy val root = (project in file("."))
  .enablePlugins(PlayJava)
  .settings(
    name := """TweetLytics""",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.13.1",
    libraryDependencies ++= Seq(
      // For Twitter OAUTH
      ws,
      guice,
      // Test Database
      "com.h2database" % "h2" % "1.4.199",
      // Testing libraries for dealing with CompletionStage...
      "org.assertj" % "assertj-core" % "3.14.0" % Test,
      "org.awaitility" % "awaitility" % "4.0.1" % Test,
      "org.twitter4j" % "twitter4j-core" % "4.0.4",
    ),
    javacOptions ++= Seq(
      "-encoding", "UTF-8",
      "-parameters",
      "-Xlint:unchecked",
      "-Xlint:deprecation",
      "-Werror"
    ),
    // Make verbose tests
    testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
  )
