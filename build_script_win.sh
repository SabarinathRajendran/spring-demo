#!/usr/bin/env bash



source ./prompt_for_multiselect.sh

Directory=('API_Gateway' 'ApplicationManagement' 'Attachment' 'AuthorizationServer' 'Delivery' 'EPRTarget' 'invoiceService')

Docker_image_name=('api-gateway' 'management-service' 'attachment-service' 'authentication-service' 'delivery-service' 'target-service' 'invoice-service')

# Create the directory to store the images
mkdir -p docker_images

exec_mvn(){
    mvn clean install
}

exec_docker(){
    docker build -t "$1":dev .
}

exec_docker_images(){
    rm "$1".tar
    docker save "$1" > "$1".tar
    cp "$1".tar ../docker_images/
}



buildOptions=('All' 'Specific')

for i in "${!buildOptions[@]}"; do
	BUILD_OPTIONS_STRING+="${buildOptions[$i]};"
done

prompt_for_multiselect BUILD_SELECTED "$BUILD_OPTIONS_STRING"

if [ "${BUILD_SELECTED[0]}" == "true" ]; 
then
    exec_mvn
	for i in "${!Directory[@]}";  do
        cd "${Directory[i]}" || exit
        exec_docker "${Docker_image_name[i]}"
        exec_docker_images "${Docker_image_name[i]}"
        cd ..
    done
else
    for i in "${!Directory[@]}"; do
        OPTIONS_STRING+="${Directory[$i]};"
    done

    prompt_for_multiselect SELECTED "$OPTIONS_STRING"

    exec_mvn

    for i in "${!SELECTED[@]}"; do
        if [ "${SELECTED[$i]}" == "true" ]; then
            
            cd "${Directory[i]}" || exit
            exec_docker "${Docker_image_name[i]}"
            exec_docker_images "${Docker_image_name[i]}"
            cd ..
        fi
    done
fi



