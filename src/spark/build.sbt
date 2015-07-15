name := "NG Reporting"

version := "1.0"

scalaVersion := "2.10.5"

val sparkVersion = "1.4.0"

resolvers += "rediscala" at "http://dl.bintray.com/etaty/maven"

libraryDependencies ++= List(
  "org.apache.kafka" %% "kafka"  % "0.8.2.0",
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-streaming" % sparkVersion,
  "org.apache.spark" %% "spark-streaming-kafka" % sparkVersion,
  "org.scala-lang.modules" %% "scala-pickling" % "0.10.1",
  "net.debasishg" %% "redisclient" % "3.0",
  "org.json4s" %% "json4s-native" % "3.2.10",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test"
)

retrieveManaged := true

exportJars := true

assemblyJarName in assembly := "reporting.jar"

mainClass in assembly := Some("com.foo.datainsights.ESBProducer")
