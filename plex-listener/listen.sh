#!/usr/bin/env bash

tail -f /plex/logs/Plex\ Media\ Server.log | python3 episode-view-event-proxy.py