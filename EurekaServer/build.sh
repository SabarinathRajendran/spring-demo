mvn clean install
sudo docker build -t naming-server:dev .
rm naming-server.tar
sudo docker save naming-server > naming-server.tar