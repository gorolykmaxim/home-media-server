import os
import re
import sys

import requests

matcher = re.compile(os.environ.get("MESSAGE_REGEXP"))
url = os.environ.get("HOME_MEDIA_APP_URL")
for line in sys.stdin:
    result = matcher.match(line)
    print('Matching: ', line)
    if result:
        print('Found match: ', result.group(1))
        requests.post(url, json={os.environ.get("ATTRIBUTE_NAME"): result.group(1)})
