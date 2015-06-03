
echo Update apt-get...
apt-get update -qq
echo Install java...
apt-get install openjdk-7-jdk -yqq

cd /tmp
if ! which scala; then
	echo Install scala..
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
cat > /usr/local/lib/spark/conf/slaves <<EOF
node1
node2
node3
EOF

su vagrant -c /dotfiles/install

su vagrant -c \
'echo "export PATH=\$PATH:/usr/local/lib/hadoop/bin:\
/usr/local/lib/kafka/bin:\
/usr/local/lib/spark/bin:\
/usr/local/lib/zookeeper/bin" > ~/.bashrc-local'

hostname | sed -e 's?node??' | tee /usr/local/lib/zookeeper/myid

cat /vagrant/hosts.txt > /etc/hosts
#cat /vagrant/platform3_key.pub >> /home/vagrant/.ssh/authorized_keys
