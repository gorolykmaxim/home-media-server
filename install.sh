#!/usr/bin/env bash

# Install docker.
sudo apt install -y docker.io docker-compose
sudo systemctl enable docker
sudo systemctl start docker

# Prepare directories for container mounting
MEDIA_FOLDER_PATH=`readlink -f ~/Downloads`
mkdir ${MEDIA_FOLDER_PATH}

# Create docker-compose.yml from template
USER_NAME=`whoami`
USER_ID=`id -u ${USER_NAME}`
GROUP_ID=`id -g ${USER_NAME}`
cp docker-compose.template.yml docker-compose.yml
sed -i "s/{userId}/${USER_ID}/g" docker-compose.yml
sed -i "s/{userGroupId}/${GROUP_ID}/g" docker-compose.yml
sed -i "s@{mediaFolderPath}@${MEDIA_FOLDER_PATH}@g" docker-compose.yml

# Deploy containers
sudo docker-compose up -d