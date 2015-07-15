#!/bin/sh
BASEDIR=$(dirname $0)

. $BASEDIR/_vars.sh

logMessage "Creating Kafka topic $KF_TOPIC_NAME.."

LOG_DIR="$KF_LOG_DIR" $KF_BIN/kafka-topics.sh \
  --zookeeper $ZK_URI \
  --create --topic "$KF_TOPIC_NAME" \
  --replication-factor 1 \
  --partitions 10

LOG_DIR="$KF_LOG_DIR" $KF_BIN/kafka-topics.sh --zookeeper $ZK_URI --list
LOG_DIR="$KF_LOG_DIR" $KF_BIN/kafka-topics.sh --describe --zookeeper $ZK_URI --topic $KF_TOPIC_NAME

logMessage "Waiting for messages to kafka topic $KF_TOPIC_NAME."
LOG_DIR="$KF_LOG_DIR" $KF_BIN/kafka-simple-consumer-shell.sh \
  --topic "$KF_TOPIC_NAME" \
  --broker-list "$KF_BROKERS" \
  --offset -1 \
  --partition 0

#test
#kafka-console-producer.sh --topic "$KF_TOPIC_NAME" --broker-list "$KF_BROKERS"
