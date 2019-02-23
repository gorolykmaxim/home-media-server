#!/usr/bin/env python3

import subprocess

qbittorrent = subprocess.Popen(['qbittorrent-nox'], stdin=subprocess.PIPE, stdout=subprocess.PIPE)
qbittorrent.stdin.write("y\n")
success_message = 'qBittorrent is successfully listening'
for line in qbittorrent.stdout:
    if success_message in line:
        qbittorrent.kill()
