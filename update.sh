#!/usr/bin/env bash

# Check if we are starting a developer environment or not
if [ "$1" == '-d' ]
then
  echo Running in development environment
  function get_root_folder_path() {
      stat -f /tmp/home-media-server
  }
  function platform_sed() {
      sed -i '' "$1" "$2"
  }
  function compose() {
      sudo docker-compose -f docker-compose.yml up -d --build
  }
else
  echo Running in production environment
  function get_root_folder_path() {
      readlink -f ~/Downloads
  }
  function platform_sed() {
      sed -i "$1" "$2"
  }
  function compose() {
      sudo docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d
  }
fi

# Prepare directories for container mounting
ROOT_FOLDER_PATH=$(get_root_folder_path)
MEDIA_FOLDER_PATH=${ROOT_FOLDER_PATH}/media
MEDIA_LOGS_FOLDER_PATH="${ROOT_FOLDER_PATH}/Library/Application Support/Plex Media Server/Logs"
mkdir -p "${ROOT_FOLDER_PATH}"
mkdir -p "${MEDIA_FOLDER_PATH}"

# Create docker-compose.yml from template
USER_NAME=$(whoami)
USER_ID=$(id -u "${USER_NAME}")
GROUP_ID=$(id -g "${USER_NAME}")
cp docker-compose.template.yml docker-compose.yml
platform_sed "s/{userId}/${USER_ID}/g" docker-compose.yml
platform_sed "s/{userGroupId}/${GROUP_ID}/g" docker-compose.yml
platform_sed "s@{configFolderPath}@${ROOT_FOLDER_PATH}@g" docker-compose.yml
platform_sed "s@{mediaFolderPath}@${MEDIA_FOLDER_PATH}@g" docker-compose.yml
platform_sed "s@{mediaLogsFolderPath}@${MEDIA_LOGS_FOLDER_PATH}@g" docker-compose.yml

# Deploy containers
compose