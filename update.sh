#!/usr/bin/env bash

# Check if we are starting a developer environment or not
if [ "$1" == '-d' ]
then
  echo Running in development environment
  function get_root_folder_path() {
      stat -f /tmp/home-media-server
  }
  function compose() {
      docker-compose -f docker-compose.yml -f docker-compose.dev.yml up -d --build
  }
else
  echo Running in production environment
  function get_root_folder_path() {
      readlink -f ~/Downloads
  }
  function compose() {
      docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d
  }
fi

# Setup environment variables for docker-compose.yml value substitution
USER_NAME=$(whoami)
export USER_ID=$(id -u "${USER_NAME}")
export GROUP_ID=$(id -g "${USER_NAME}")
export ROOT_FOLDER_PATH=$(get_root_folder_path)
export MEDIA_FOLDER_PATH=${ROOT_FOLDER_PATH}/media

# Prepare directories for container mounting
mkdir -p "${ROOT_FOLDER_PATH}"
mkdir -p "${MEDIA_FOLDER_PATH}"

# Deploy containers
compose