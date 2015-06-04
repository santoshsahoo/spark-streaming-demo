name := "NG Reporting"

version := "1.0"

scalaVersion := "2.10.4"

libraryDependencies ++= List(
  "org.apache.kafka" %% "kafka"  % "0.8.2.0",
  "org.apache.spark" %% "spark-core" % "1.3.0" % "provided",
  "org.apache.spark" %% "spark-streaming" % "1.3.0",
  "org.apache.spark" %% "spark-streaming-kafka" % "1.3.0",
  "org.json4s" %% "json4s-native" % "3.2.10",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test"
)

retrieveManaged := true

assemblyJarName in assembly := "reporting.jar"

mainClass in assembly := Some("com.foo.datainsights.ESBProducer")
