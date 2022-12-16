name := "dip22-assignment"
version := "1.0"
scalaVersion := "2.12.16"

val SparkVersion: String = "3.3.0"
val ScalaTestVersion: String = "3.2.13"

libraryDependencies += "org.apache.spark" %% "spark-core" % SparkVersion
libraryDependencies += "org.apache.spark" %% "spark-mllib" % SparkVersion
libraryDependencies += "org.apache.spark" %% "spark-sql" % SparkVersion
libraryDependencies += "org.apache.spark" %% "spark-streaming" % SparkVersion
libraryDependencies += "org.scalatest" %% "scalatest" % ScalaTestVersion % "test"

// suppress all log messages when setting up the Spark Session
javaOptions += "-Dlog4j.configurationFile=project/log4j.properties"

// in order to be able to use the javaOptions defined above
Test / fork := true

scalaVersion := "2.13.10"

libraryDependencies  ++= Seq(
  // other dependencies here
  "org.scalanlp" %% "breeze" % "1.3",
  // native libraries are not included by default. add this if you want them (as of 0.7)
  // native libraries greatly improve performance, but increase jar sizes.
  // It also packages various blas implementations, which have licenses that may or may not
  // be compatible with the Apache License. No GPL code, as best I know.
  "org.scalanlp" %% "breeze-natives" % "1.3",
  // the visualization library is distributed separately as well.
  // It depends on LGPL code.
  "org.scalanlp" %% "breeze-viz" % "1.3"
)

libraryDependencies += "io.github.pityka" %% "nspl-awt" % "0.5.0"
