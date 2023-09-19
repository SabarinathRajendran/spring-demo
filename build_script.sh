#!/usr/bin/env bash

Directory=('API_Gateway' 'ApplicationManagement' 'Attachment' 'AuthorizationServer' 'Delivery' 'EPRTarget' 'invoiceService')

Docker_image_name=('api-gateway' 'management-service' 'attachment-service' 'authentication-service' 'delivery-service' 'target-service' 'invoice-service')

# Create the directory to store the images
mkdir -p docker_images

exec_mvn(){
    mvn clean install
}

exec_docker(){
    sudo docker build -t $1:dev .
}

exec_docker_images(){
    rm $1.tar
    sudo docker save $1 > $1.tar
    sudo cp $1.tar ../docker_images/
}


exec_mvn
for i in ${!Directory[@]};  do
    cd ${Directory[i]}
    # exec_mvn
    exec_docker ${Docker_image_name[i]}
    cd ..
done