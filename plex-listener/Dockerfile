FROM python:3
COPY ./listen.sh .
COPY ./episode-view-event-proxy.py .
COPY ./requiremenets.txt .
RUN pip install -r requiremenets.txt
ENTRYPOINT ["./listen.sh"]