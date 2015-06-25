#!/bin/sh
export HADOOP_HOME='/var/hadoop'

sudo rm -rf /var/zookeeper
sudo rm -rf /var/kafka
sudo rm -rf $HADOOP_HOME

for dir in zookeeper kafka hadoop; do mkdir /var/$dir; done
for dir in name data logs; do mkdir $HADOOP_HOME/$dir; done

export JAVA_HOME='/usr/lib/jvm/java-7-openjdk-amd64'
/usr/local/lib/hadoop/bin/hdfs namenode -format
chmod -R 777 $HADOOP_HOME
chown vagrant:vagrant -R $HADOOP_HOME
