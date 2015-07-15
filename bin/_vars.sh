RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

export JAVA_HOME='/usr/lib/jvm/java-7-openjdk-amd64'

ZOO_BIN='/usr/local/lib/zookeeper/bin'
ZOO_LOG_DIR='/var/zookeeper/1'
ZK_CFGFILE='zookeeper/zoo1.cfg'
ZK_URI='node1:2181/expensekafka'

KF_BIN='/usr/local/lib/kafka/bin'
KF_LOG_DIR='/var/kafka/1/logs'
KF_DATA_DIR='/var/kafka/1/data'
KF_CFGFILE='kafka1.properties'
KF_TOPIC_NAME="expense.reports"
KF_BROKERS="node1:9092"

SPARK_SBIN="/usr/local/lib/spark/sbin"
SPARK_LOG_DIR='/tmp/spark-log'

logError(){
  printf "${RED}${1}${NC}\n"
}

logSuccess(){
  printf "${GREEN}${1}${NC}\n"
}

logMessage(){
  printf "${BLUE}${1}${NC}\n"
}
