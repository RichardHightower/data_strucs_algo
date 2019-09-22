lazy val data_structs = (project in file("."))
  .settings(
    name := "data_structs",
    organization := "rick",
    scalaVersion := "2.13.0",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % Test,
  )
