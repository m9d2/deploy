#!/bin/bash
SERVER_PORT=$1
INDEX=0
APP_NAME=$(ls target/*.jar)
pid=$(ps -ef | grep "$APP_NAME" | grep index="$INDEX" | grep -v grep | awk '{print $2}')
if [ -n "$pid" ]
then
  echo "kill -9 pid:" "$pid"
  kill -9 "$pid"
fi
source /etc/profile
nohup java -server -Xms60m -Xmx100m -jar "$APP_NAME" --index="$INDEX" --server.port="$SERVER_PORT" >/dev/null &2>&1 &
echo 部署完成...
