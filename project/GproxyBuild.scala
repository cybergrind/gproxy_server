import sbt.Keys._
import sbt._
import sbtassembly.AssemblyPlugin.autoImport._
import org.scalajs.sbtplugin.ScalaJSPlugin
import ScalaJSPlugin.autoImport._


object GproxyBuild extends Build {

  // TODO: clean unused libraries
  val baseSettings = Seq(
    scalacOptions ++= Seq(
      "-deprecation",
      "-encoding", "UTF-8",
      "-feature",
      "-language:existentials",
      "-language:experimental.macros",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-unchecked",
      "-Yno-adapted-args",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen",
      "-Xfuture",
      "-Xlint"
      // ,"-Ymacro-debug-lite"
    ),
    resolvers += Resolver.sonatypeRepo("snapshots"),
    resolvers += Resolver.sonatypeRepo("releases"),
    scalaVersion := "2.11.8"
  )

  val akkaV = "2.4.8"
  val specsV = "3.7.2"
  val sprayV = "1.3.3"
  val baseDeps = Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    //"com.chuusai" %% "shapeless" % "2.3.0",
    //"org.scala-lang" % "scala-reflect" % "2.11.8",
    //"org.scalaz" %% "scalaz-core" % "7.2.2",
    "com.typesafe" % "config" % "1.3.0",
    "org.slf4j" % "slf4j-log4j12" % "1.7.21",
    "io.spray" %% "spray-can" % sprayV,
    "io.spray" %% "spray-routing-shapeless2" % sprayV,
    "io.spray" %% "spray-httpx" % sprayV,
    "io.spray" %% "spray-json" % "1.3.2",
    //"com.typesafe.play" %% "play-json" % "2.5.1",
    //"org.java-websocket" % "Java-WebSocket" % "1.3.0",
    "com.github.fommil" %% "spray-json-shapeless" % "1.2.0"
    //"com.typesafe.akka" %% "akka-stream-experimental" % "2.0.4"
  )

  val testDeps = Seq(
    "com.typesafe.akka" %% "akka-testkit" % akkaV,
    "org.specs2" %% "specs2-core" % specsV,
    "org.specs2" %% "specs2-matcher-extra" % specsV,
    "io.spray" %% "spray-testkit" % sprayV
  )

  val commonSettings = baseSettings ++ Seq(
    organization := "kpi",
    version := "0.2.0",
    licenses +=("MIT", url("http://opensource.org/licenses/MIT")),
    libraryDependencies ++= baseDeps
  )

  val js = Seq(
    scalaJSUseRhino in Global := false
  )

  val mainSettings = commonSettings ++ Seq(
    mainClass in assembly := Some("kpi.gproxy.Boot"),
    test in assembly := {}
  )

  def makeProject(name: String, path: Option[String] = None,
                  settings: Seq[sbt.Def.Setting[_]] = commonSettings) = {
    Project(
      id = name,
      base = file(path getOrElse name),
      settings = settings
    )
  }


  lazy val front = makeProject("front").
    enablePlugins(ScalaJSPlugin).
    settings(
      js
    )

  lazy val core = makeProject("core")


  lazy val main = makeProject("main", Some("."), mainSettings)
    .dependsOn(core)
    .aggregate(core)
    .settings(
    resources in Compile ++= Seq(
      (fastOptJS in Compile in front).value.data
    ),
      unmanagedResourceDirectories in Compile += {
        println(baseDirectory( _ / "front" / "src" / "main" / "resources").value)
        baseDirectory( _ / "front" / "src" / "main" / "resources").value
      }
    )
}
