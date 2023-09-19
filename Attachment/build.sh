mvn clean install
sudo docker build -t attachment-service:dev .
rm attachment-service.tar
sudo docker save attachment-service > attachment-service.tar