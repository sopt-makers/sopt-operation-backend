#!/bin/bash

CURRENT_RUNNING_PORT=$(cat /etc/nginx/conf.d/service-url.inc | grep -Po '[0-9]+' | tail -1)

echo "> Nginx currently proxies to ${CURRENT_RUNNING_PORT}."

sudo service nginx reload

echo "> Nginx reloaded."

echo "> curl -s http://127.0.0.1:$CURRENT_RUNNING_PORT/api/v1/test"
echo "> Now Nginx proxies to ${CURRENT_RUNNING_PORT}."
