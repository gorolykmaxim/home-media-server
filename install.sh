#!/usr/bin/env bash

# Install docker.
sudo apt install -y docker.io docker-compose
sudo systemctl enable docker
sudo systemctl start docker

./update.sh