mvn clean install
sudo docker build -t authentication-service:dev .
rm authentication-service.tar
sudo docker save authentication-service > authentication-service.tar