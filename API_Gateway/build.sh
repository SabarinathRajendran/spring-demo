mvn clean install
sudo docker build -t api-gateway:dev .
rm api-gateway.tar
sudo docker save api-gateway > api-gateway.tar