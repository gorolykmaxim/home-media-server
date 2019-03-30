import requests
import json
import re
import sys

matcher = re.compile(r"\'(.*)\'( -)? got played")
url = 'http://home-media-app/api/v1/episode/view'
for line in sys.stdin:
    result = matcher.match(line)
    print('Matching: ', line)
    if result:
        print('Found match: ', result.group(1))
        requests.post(url, data=json.dumps({'episodeName': result.group(1)}))
