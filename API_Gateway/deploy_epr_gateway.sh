docker load < docker_images/api-gateway.tar
docker stop api-gateway
docker rm api-gateway
docker run -d -p 8765:8765 -e POSTGRES_USERNAME='' -e POSTGRES_PASSWORD='' --name=api-gateway api-gateway:dev