ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

ThisBuild / libraryDependencies += "org.apache.kafka" % "kafka-clients" % "3.0.0"
ThisBuild / libraryDependencies += "org.apache.kafka" %% "kafka-streams-scala" % "3.0.0"
ThisBuild / libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
ThisBuild / libraryDependencies +="org.apache.spark" %% "spark-core" % "3.5.0"
ThisBuild / libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.5.0"
ThisBuild / libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.30"


lazy val root = (project in file("."))
  .settings(
    name := "pfsd-project-data-processor",
    idePackagePrefix := Some("eci.edu.co")
  )
