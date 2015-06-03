#!/bin/sh
# | kafka-console-producer.sh --topic "expense.reports" --broker-list node1:9092
#while read line; do echo "$line"; sleep $1; done  < sampledata.txt

n=1
s=0

if [ ! -z $1 ]; then n=$1; fi
if [ ! -z $2 ]; then s=$2; fi

i=0
while [ $i -lt $n ]; do
  echo "{entity:1, \"expense\": $i, \"title\": \"report 1\", \"sub\": 2000, \"date\": \"01/01/2015\"}"
  i=$((i+1))
  sleep $s
done
