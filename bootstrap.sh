
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

cat >> /etc/profile <<EOF
export PATH=$PATH:/usr/local/lib/hadoop/bin:/usr/local/lib/kafka/bin:/usr/local/lib/spark/bin:/usr/local/lib/zookeeper/bin
EOF

hostname | sed -e 's?node??' | tee /usr/local/lib/zookeeper/myid

cat >> /etc/hosts<<EOF
192.168.20.11 node1
192.168.20.12 node2
192.168.20.13 node3
EOF
