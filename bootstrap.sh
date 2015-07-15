#!/bin/sh

echo Updating apt-get...
apt-get update -yqq
echo Installing java...
apt-get install openjdk-7-jdk -yqq
pip install --upgrade fabric

cd /tmp
if ! which scala; then
	echo Installing scala..
	wget www.scala-lang.org/files/archive/scala-2.10.4.deb
	dpkg -i scala-2.10.4.deb
fi

if ! which sbt; then
	echo "deb http://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list
	sudo apt-get -yq update
	sudo apt-get -fyq --force-yes install sbt
fi

echo Fix pending dependencies..
apt-get upgrade -qyf

echo Install hadoop...
/vagrant/bootstrap/hadoop-install
echo Install zookeeper...
/vagrant/bootstrap/zookeeper-install
echo Install kafka...
/vagrant/bootstrap/kafka-install
echo Install spark...
/vagrant/bootstrap/spark-install

su vagrant -c /dotfiles/install

su vagrant -c \
'echo "export PATH=\$PATH:/usr/local/lib/hadoop/bin:\
/usr/local/lib/kafka/bin:\
/usr/local/lib/spark/bin:\
/usr/local/lib/zookeeper/bin" > ~/.bashrc-local'

hostname | sed -e 's?node??' | tee /usr/local/lib/zookeeper/myid

cat /vagrant/hosts.txt > /etc/hosts
#cat /vagrant/platform3_key.pub >> /home/vagrant/.ssh/authorized_keys
#http://unix.stackexchange.com/questions/33271/how-to-avoid-ssh-asking-permission

su vagrant -c \
'
rm ~/.ssh/id_dsa*
cat /vagrant/ssh-config.txt > ~/.ssh/config
ssh-keygen -t dsa -P "" -f ~/.ssh/id_dsa
cat ~/.ssh/id_dsa.pub >> ~/.ssh/authorized_keys
ssh node1 "echo Hello from \$HOSTNAME"
'
killall -9 java

cp /vagrant/spark/conf/* /usr/local/lib/spark/conf/
cp /vagrant/hadoop/etc/* /usr/local/lib/hadoop/etc/hadoop/

export HADOOP_HOME='/var/hadoop'
export HADOOP_LOG_DIR=$HADOOP_HOME/logs
rm -rf $HADOOP_HOME
for dir in name data logs; do mkdir -p $HADOOP_HOME/$dir; done

export JAVA_HOME='/usr/lib/jvm/java-7-openjdk-amd64'
/usr/local/lib/hadoop/bin/hdfs namenode -format
chmod -R 777 $HADOOP_HOME
chown vagrant:vagrant -R $HADOOP_HOME

apt-get install -yqq redis-server postgresql nodejs npm
npm install forever express hiredis redis -g
sudo ln -s /usr/bin/nodejs /usr/bin/node
