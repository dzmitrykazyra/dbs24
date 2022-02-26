sudo /home/kafka/kafka/bin/kafka-topics.sh --zookeeper 127.0.0.1:2181 --list

sudo /home/kafka/kafka/bin/kafka-topics.sh --zookeeper 127.0.0.1:2181 --topic insta_Accounts –delete
sudo /home/kafka/kafka/bin/kafka-topics.sh --zookeeper 127.0.0.1:2181 --topic insta_Posts –delete
sudo /home/kafka/kafka/bin/kafka-topics.sh --zookeeper 127.0.0.1:2181 --topic insta_Sources –delete
sudo /home/kafka/kafka/bin/kafka-topics.sh --zookeeper 127.0.0.1:2181 --topic insta_Pictures –delete
sudo /home/kafka/kafka/bin/kafka-topics.sh --zookeeper 127.0.0.1:2181 --topic insta_SourcesTasks –delete
sudo /home/kafka/kafka/bin/kafka-topics.sh --zookeeper 127.0.0.1:2181 --topic insta_Vectors --delete

sudo /home/kafka/kafka/bin/kafka-topics.sh --zookeeper 127.0.0.1:2181 --list