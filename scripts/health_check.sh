#!/bin/bash

CURRENT_RUNNING_PORT=$(cat /etc/nginx/conf.d/service-url.inc | grep -Po '[0-9]+' | tail -1)

if [ ${CURRENT_RUNNING_PORT} -ne 8081 -o ${CURRENT_RUNNING_PORT} -ne 8082 ]; then
    # run_new_was 에서 정상적으로 처리되지 않았다는 의미 - 더 이상 진행되면 안된다.
    echo "> No WAS is connected to nginx"
    exit 1
fi

echo "> Start health check of WAS at 'http://127.0.0.1:${CURRENT_RUNNING_PORT}/api/v1/test' ..."

for RETRY_COUNT in {1..10}
do
    echo "> #${RETRY_COUNT} trying..."
    RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}"  http://127.0.0.1:${CURRENT_RUNNING_PORT}/api/v1/test)

    if [ ${RESPONSE_CODE} -eq 200 ]; then
        echo "> New WAS successfully running"
        exit 0
    elif [ ${RETRY_COUNT} -eq 10 ]; then
        echo "> Health check failed."
        exit 1
    fi
    sleep 10
done