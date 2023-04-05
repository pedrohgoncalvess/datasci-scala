ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "datasci-scala"
  )

libraryDependencies += "org.apache.spark" %% "spark-mllib" % "3.3.2"