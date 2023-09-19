mvn clean install
sudo docker build -t delivery-service:dev .
rm delivery-service.tar
sudo docker save delivery-service > delivery-service.tar