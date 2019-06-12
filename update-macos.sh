#!/usr/bin/env bash

# Prepare directories for container mounting
ROOT_FOLDER_PATH=`stat -f ~/Downloads`
MEDIA_FOLDER_PATH=${ROOT_FOLDER_PATH}/media
MEDIA_LOGS_FOLDER_PATH="${ROOT_FOLDER_PATH}/Library/Application Support/Plex Media Server/Logs"
mkdir ${ROOT_FOLDER_PATH}
mkdir ${MEDIA_FOLDER_PATH}

# Create docker-compose.yml from template
USER_NAME=`whoami`
USER_ID=`id -u ${USER_NAME}`
GROUP_ID=`id -g ${USER_NAME}`
cp docker-compose.prod.template.yml docker-compose.prod.yml
sed -i '' "s/{userId}/${USER_ID}/g" docker-compose.prod.yml
sed -i '' "s/{userGroupId}/${GROUP_ID}/g" docker-compose.prod.yml
sed -i '' "s@{configFolderPath}@${ROOT_FOLDER_PATH}@g" docker-compose.prod.yml
sed -i '' "s@{mediaFolderPath}@${MEDIA_FOLDER_PATH}@g" docker-compose.prod.yml
sed -i '' "s@{mediaLogsFolderPath}@${MEDIA_LOGS_FOLDER_PATH}@g" docker-compose.prod.yml

# Deploy containers
sudo docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d --build