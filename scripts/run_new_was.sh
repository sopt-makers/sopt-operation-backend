#!/bin/bash

echo "> Reun New WAS Start"

# 현재 proxy 중인 실구동 port
CURRENT_RUNNING_PORT=$(cat /etc/nginx/conf.d/service-url.inc | grep -Po '[0-9]+' | tail -1)

echo "> Current port of running WAS is ${CURRENT_RUNNING_PORT}."

if [ ${CURRENT_RUNNING_PORT} -eq 8081 ]; then
  NEXT_RUNNING_PORT=8082
elif [ ${CURRENT_RUNNING_PORT} -eq 8082 ]; then
  NEXT_RUNNING_PORT=8081
else
  echo "> No WAS is connected to nginx"
  NEXT_RUNNING_PORT=8081
fi

# 혹시나 다른 프로세스가 실행되고 있을 경우를 대비하여 PID Kill
CHECK_PID=$(lsof -Fp -i TCP:${NEXT_RUNNING_PORT} | grep -Po 'p[0-9]+' | grep -Po '[0-9]+')

if [ ! -z ${CHECK_PID} ]; then
  echo "> Kill WAS running at ${NEXT_RUNNING_PORT}."
  sudo kill -15 ${CHECK_PID}
  sleep 5
fi


if [ "$DEPLOYMENT_GROUP_NAME" == "prod" ]
then
   nohup java -jar -Dserver.port=${NEXT_RUNNING_PORT} -Dspring.profiles.active=prod /home/ubuntu/operation/operation-api/build/libs/operation-api-0.0.1-SNAPSHOT.jar > /dev/null 2> /dev/null < /dev/null &
   echo "> Now new WAS runs at ${NEXT_RUNNING_PORT}."
fi

if [ "$DEPLOYMENT_GROUP_NAME" == "dev" ]
then
   nohup java -jar -Dserver.port=${NEXT_RUNNING_PORT} -Dspring.profiles.active=dev /home/ubuntu/operation/operation-api/build/libs/operation-api-0.0.1-SNAPSHOT.jar > /dev/null 2> /dev/null < /dev/null &
   echo "> Now new WAS runs at ${NEXT_RUNNING_PORT}."
fi
sleep 10

echo "set \$service_url http://127.0.0.1:${NEXT_RUNNING_PORT};" |sudo tee /etc/nginx/conf.d/service-url.inc

exit 0
