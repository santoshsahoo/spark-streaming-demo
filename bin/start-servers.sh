#!/bin/sh
BASEDIR=$(dirname $0)

. $BASEDIR/_vars.sh

sudo mkdir -p /var/zookeeper
sudo mkdir -p /var/kafka

sudo chown vagrant:vagrant /var/zookeeper
sudo chown vagrant:vagrant /var/kafka

logMessage "Starting Hadoop.."
/usr/local/lib/hadoop/sbin/start-dfs.sh

logMessage "Starting ZoooKeeper.."

mkdir -p "$ZOO_LOG_DIR"
ZOO_LOG_DIR="$ZOO_LOG_DIR" $ZOO_BIN/zkServer.sh start "$PWD/$ZK_CFGFILE"

logMessage "Starting Kafka.."

mkdir -p $KF_LOG_DIR
mkdir -p $KF_DATA_DIR
LOG_DIR="$KF_LOG_DIR" $KF_BIN/kafka-server-start.sh -daemon "$PWD/$KF_CFGFILE"

#wait for kafka to come alive
sleep 10

logMessage "Starting Spark.."

SPARK_LOG_DIR="$SPARK_LOG_DIR" $SPARK_SBIN/start-master.sh
SPARK_LOG_DIR="$SPARK_LOG_DIR" SPARK_WORKER_DIR="/tmp/spark-work" \
  $SPARK_SBIN/start-slave.sh spark://node1:7077

sleep 5

[ $(jps | wc -l) -gt 7 ] && logSuccess "Everything looks good"
[ $(jps | wc -l) -lt 8 ] && logError "Something does not look good, check jps"

jps -m
