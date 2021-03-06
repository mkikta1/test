import scala.util.matching.Regex

import sbt.Keys._
import sbt._

object Common {

  val projectSettings = Seq(
    organizationName := "TheHive-Project",
    organization := "org.thehive-project",
    licenses += "AGPL-V3" → url("https://www.gnu.org/licenses/agpl-3.0.html"),
    organizationHomepage := Some(url("http://thehive-project.org/")),
    resolvers += Resolver.bintrayRepo("thehive-project", "maven"),
    resolvers += "elasticsearch-releases" at "https://artifacts.elastic.co/maven",
    scalaVersion := Dependencies.scalaVersion,
    scalacOptions ++= Seq(
      "-deprecation", // Emit warning and location for usages of deprecated APIs.
      "-feature", // Emit warning and location for usages of features that should be imported explicitly.
      "-unchecked", // Enable additional warnings where generated code depends on assumptions.
      //"-Xfatal-warnings", // Fail the compilation if there are any warnings.
      "-Xlint", // Enable recommended additional warnings.
      "-Ywarn-adapted-args", // Warn if an argument list is modified to match the receiver.
      "-Ywarn-dead-code", // Warn when dead code is identified.
      "-Ywarn-inaccessible", // Warn about inaccessible types in method signatures.
      "-Ywarn-nullary-override", // Warn when non-nullary overrides nullary, e.g. def foo() over def foo.
      "-Ywarn-numeric-widen" // Warn when numerics are widened.
    ),
    scalacOptions in Test ~= { options ⇒
      options filterNot (_ == "-Ywarn-dead-code") // Allow dead code in tests (to support using mockito).
    },
    parallelExecution in Test := false,
    fork in Test := true,
    javaOptions += "-Xmx1G",

    // Redirect logs from ElasticSearch (which uses log4j2) to slf4j
    libraryDependencies += "org.apache.logging.log4j" % "log4j-to-slf4j" % "2.9.1",
    excludeDependencies += "org.apache.logging.log4j" % "log4j-core"
  )

  val stableVersion: Regex = "(\\d+\\.\\d+\\.\\d+)-(\\d+)".r
  val betaVersion: Regex = "(\\d+\\.\\d+\\.\\d+)-[Rr][Cc](\\d+)".r
  object snapshotVersion {
    def unapplySeq(version: String): Option[List[String]] = {
      if (version.endsWith("-SNAPSHOT")) {
        val v = version.dropRight(9)
        stableVersion.unapplySeq(v) orElse betaVersion.unapplySeq(v)
      }
      else None
    }
  }
}