#!/bin/sh
BASEDIR=$(dirname $0)

. $BASEDIR/_vars.sh

RUNNING=1
logMessage "Stopping Hadoop.."
/usr/local/lib/hadoop/sbin/stop-dfs.sh

logMessage "Stopping ZoooKeeper.."
ZOO_LOG_DIR="$ZOO_LOG_DIR" $ZOO_BIN/zkServer.sh stop "$PWD/$ZK_CFGFILE"

logMessage "Stopping Kafka.."
LOG_DIR="$KF_LOG_DIR" $KF_BIN/kafka-server-stop.sh "$PWD/$KF_CFGFILE" 2>/dev/null

SPARK_LOG_DIR="$SPARK_LOG_DIR" /usr/local/lib/spark/sbin/stop-master.sh

sleep 10
killall -9 java 2>/dev/null

logMessage "Still running (if any)"

jps -m
