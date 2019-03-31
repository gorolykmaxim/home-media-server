import re
import sys

import requests

matcher = re.compile(r".*\'(.*)\'( -)? got played.*")
url = 'http://home-media-app:8080/api/v1/episode/view'
for line in sys.stdin:
    result = matcher.match(line)
    print('Matching: ', line)
    if result:
        print('Found match: ', result.group(1))
        requests.post(url, json={'episodeName': result.group(1)})
