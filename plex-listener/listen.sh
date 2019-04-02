#!/usr/bin/env bash

tail -F /plex/logs/Plex\ Media\ Server.log | python3 episode-view-event-proxy.py