import sbt._
import Keys._
import sbtassembly.Plugin._
import AssemblyKeys._

seq(assemblySettings: _*)

name := "Yottabyte-Spark-Examples"

version := "1.0"

scalaVersion := "2.10.4"

retrieveManaged in ThisBuild := true

libraryDependencies += "org.apache.spark" % "spark-core_2.10" % "1.1.0"

libraryDependencies += "org.specs2" %% "specs2" % "1.12.3" withSources() withJavadoc()

libraryDependencies += "junit" % "junit" % "4.10" withSources() withJavadoc()

libraryDependencies += "org.scalaz.stream" % "scalaz-stream_2.10" % "0.1"

excludedJars in assembly <<= (fullClasspath in assembly) map { cp =>
  val excludes = Set(
    "jsp-api-2.1-6.1.14.jar",
    "jsp-2.1-6.1.14.jar",
    "jasper-compiler-5.5.12.jar",
    "commons-beanutils-core-1.8.0.jar",
    "commons-beanutils-1.7.0.jar",
    "servlet-api-2.5-20081211.jar",
    "servlet-api-2.5.jar",
    "jcl-over-slf4j-1.7.5.jar"
  )
  cp filter { jar => excludes(jar.data.getName) }
}

mergeStrategy in assembly <<= (mergeStrategy in assembly) {
  (old) => {
    case x if x.startsWith("META-INF") => MergeStrategy.discard // Bumf
    case x if x.startsWith("plugin.properties") => MergeStrategy.discard // Bumf
    case x if x.endsWith(".html") => MergeStrategy.discard // More bumf
    case PathList("com", "esotericsoftware", xs @ _*) => MergeStrategy.last // For Log$Logger.class
    case x => old(x)
  }
}