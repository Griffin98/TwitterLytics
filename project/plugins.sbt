// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.8.2")

// Play enhancer - this automatically generates getters/setters for public fields
// and rewrites accessors of these fields to use the getters/setters. Remove this
// plugin if you prefer not to have this feature, or disable on a per project
// basis using disablePlugins(PlayEnhancer) in your build.sbt
addSbtPlugin("com.typesafe.sbt" % "sbt-play-enhancer" % "1.2.2")

addSbtPlugin("com.github.sbt" % "sbt-jacoco" % "3.2.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-coffeescript" % "1.0.2")