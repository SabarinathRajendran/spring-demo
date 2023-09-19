docker load < docker_images/delivery-service.tar
docker stop delivery-service
docker rm delivery-service
docker run -d -p 8092:8092 -e POSTGRES_USERNAME='' -e POSTGRES_PASSWORD='' --name=delivery-service delivery-service:dev