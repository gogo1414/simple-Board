#!/bin/bash

REPOSITORY=/home/ec2-user/app/step2

echo "> 현재 구동 중인 애플리케이션 pid 확인"

#CURRENT_PID=$(pgrep -fl wnstn6945-springboot3-board | grep jar | awk '{print $1}')

CURRENT_PID=$(pgrep -f ${REPOSITORY}.*.jar)

echo "현재 구동 중인 애플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
  ehco "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> Build 파일 복사"

cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> 새 애플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR NAME: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

nohup java -jar \
    -Dspring.config.location=classpath:/application-real.yml,/home/ec2-user/app/application-oauth.yml,/home/ec2-user/app/application-real-db.yml \
    -Dspring.profiles.active=real \
    $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &