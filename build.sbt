name := "app55-scala"

version := "0.8.0"

//libraryDependencies += "org.codehaus.jackson" % "jackson-core-asl" % "1.9.13"

//libraryDependencies += "org.codehaus.jackson" % "jackson-mapper-asl" % "1.9.13"

mainClass in (Compile, run) := Some("com.app55.test.integration")

//libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.10.1" % "test"

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.0" % "test"

//libraryDependencies += "net.debasishg" % "sjson_2.10" % "0.19"

libraryDependencies += "com.fasterxml.jackson.module" % "jackson-module-scala_2.10" % "2.3.1"
