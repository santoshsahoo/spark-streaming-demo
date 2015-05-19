#!/bin/sh

COUNT=3
SEQ=$(seq 1 $COUNT)
ZK_BIN='/usr/local/lib/zookeeper/bin'
ZK_DATABASE='/tmp/zookeeper'
ZK_CFBASE='/vagrant/zookeeper'

for n in $SEQ; do
  ZK_CFGFILE="$ZK_CFBASE/zoo${n}.cfg"
  ZK_DATA="$ZK_DATABASE/${n}"

  mkdir -p "$ZK_DATA/log"
  echo $n > "$ZK_DATA/myid"

  NODE=$n ./templater.sh zoo.cfg.tmpl > "$ZK_CFGFILE"

  echo ZOO_LOG_DIR="$ZK_DATA/log" $ZK_BIN/zkServer.sh start-foreground "$ZK_CFGFILE"
done

echo "Running.."; exit;
read line

for n in $SEQ; do
  ZK_CFGFILE="$ZK_CFBASE/zoo${n}.cfg"
  ZK_DATA="$ZK_DATABASE/${n}"

  ZOO_LOG_DIR="$ZK_DATA/log" $ZK_BIN/zkServer.sh stop "$ZK_CFGFILE"
done
