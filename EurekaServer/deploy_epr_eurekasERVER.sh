docker load < docker_images/naming-server.tar
docker stop naming-server
docker rm naming-server
docker run -d -p 8761:8761 --name=naming-server naming-server:dev