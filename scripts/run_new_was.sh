#!/bin/bash

CURRENT_PORT=$(cat /etc/nginx/conf.d/service-url.inc | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0

echo "> Current port of running WAS is ${CURRENT_PORT}."

if [ ${CURRENT_PORT} -eq 8081 ]; then
  TARGET_PORT=8082
elif [ ${CURRENT_PORT} -eq 8082 ]; then
  TARGET_PORT=8081
else
  echo "> No WAS is connected to nginx"
fi

TARGET_PID=$(lsof -Fp -i TCP:${TARGET_PORT} | grep -Po 'p[0-9]+' | grep -Po '[0-9]+')

if [ ! -z ${TARGET_PID} ]; then
  echo "> Kill WAS running at ${TARGET_PORT}."
  sudo kill -15 ${TARGET_PID}
fi

if [ "$DEPLOYMENT_GROUP_NAME" == "prod" ]
then
   nohup java -jar -Dserver.port=${TARGET_PORT} -Dspring.profiles.active=prod /home/ubuntu/operation/operation-api/build/libs/operation-api-0.0.1-SNAPSHOT.jar > /dev/null 2> /dev/null < /dev/null &
   echo "> Now new WAS runs at ${TARGET_PORT}."
fi

if [ "$DEPLOYMENT_GROUP_NAME" == "dev" ]
then
   nohup java -jar -Dserver.port=${TARGET_PORT} -Dspring.profiles.active=dev /home/ubuntu/operation/operation-api/build/libs/operation-api-0.0.1-SNAPSHOT.jar > nohup.out 2>&1 </dev/null &
   echo "> Now new WAS runs at ${TARGET_PORT}."
fi
sleep 10

exit 0
