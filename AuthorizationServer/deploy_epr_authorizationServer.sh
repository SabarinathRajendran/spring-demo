docker load < docker_images/authentication-service.tar
docker stop authentication-service
docker rm authentication-service
docker run -d -p 8555:8555 -e POSTGRES_USERNAME='' -e POSTGRES_PASSWORD='' --name=authentication-service authentication-service:dev