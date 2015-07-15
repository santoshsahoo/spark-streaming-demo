#!/bin/sh
BASEDIR=$(dirname $0)

. $BASEDIR/_vars.sh

export HADOOP_HOME='/var/hadoop'

rm -rf /var/zookeeper
rm -rf $KF_DATA_DIR
rm -rf $HADOOP_HOME

mkdir -p $HADOOP_HOME
chmod -R 777 $HADOOP_HOME

for dir in zookeeper kafka hadoop; do mkdir /var/$dir; done
for dir in name data logs; do mkdir $HADOOP_HOME/$dir; done

/usr/local/lib/hadoop/bin/hdfs namenode -format

chown vagrant:vagrant -R $HADOOP_HOME
