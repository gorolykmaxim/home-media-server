#!/usr/bin/env bash

# Install plex media server.
wget https://downloads.plex.tv/plex-media-server/1.14.1.5488-cc260c476/plexmediaserver_1.14.1.5488-cc260c476_amd64.deb
sudo apt install ./plexmediaserver*.deb
# Install torrent client.
USER=`whoami`
sudo add-apt-repository ppa:qbittorrent-team/qbittorrent-stable -y
sudo apt-get update && sudo apt-get install -y qbittorrent-nox
# Configure torrent client.
sudo cp qbittorrent.service /etc/systemd/system/
sudo sed -i "s/{username}/$USER/g" /etc/systemd/system/qbittorrent.service
sudo systemctl daemon-reload
sudo su $USER -c qbittorrent-nox << EOF
y
EOF
sudo systemctl enable qbittorrent
sudo systemctl start qbittorrent
