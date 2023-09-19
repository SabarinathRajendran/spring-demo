docker load < docker_images/attachment-service.tar
docker stop attachment-service
docker rm attachment-service
docker run -d -p 8080:8080 -e POSTGRES_USERNAME='' -e POSTGRES_PASSWORD='' --name=attachment-service attachment-service:dev