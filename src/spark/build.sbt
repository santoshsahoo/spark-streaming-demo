name := "NG Reporting"

version := "1.0"

scalaVersion := "2.10.4"

libraryDependencies ++= List(
  "org.slf4j" % "log4j-over-slf4j" % "1.7.10",
  "org.apache.kafka" %% "kafka"  % "0.8.2.0",
  "org.apache.spark" %% "spark-core" % "1.3.0" % "provided",
  "org.apache.spark" %% "spark-sql" % "1.3.0",
  "org.apache.spark" %% "spark-hive" % "1.3.0",
  "org.apache.spark" %% "spark-streaming" % "1.3.0",
  "org.apache.spark" %% "spark-streaming-kafka" % "1.3.0",
  "org.apache.spark" %% "spark-mllib" % "1.3.0",
  "com.twitter" %% "parquet" % "2.1.0",
  "mysql" % "mysql-connector-java" % "5.1.31",
  "com.datastax.spark" %% "spark-cassandra-connector" % "1.0.0-rc5",
  "com.datastax.spark" %% "spark-cassandra-connector-java" % "1.0.0-rc5",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test"
)
