#!/usr/bin/env python3

import subprocess

qbittorrent = subprocess.Popen(['qbittorrent-nox'], stdin=subprocess.PIPE, stdout=subprocess.PIPE)
qbittorrent.stdin.write(b"y\n")
qbittorrent.stdout.readline(1)
qbittorrent.kill()