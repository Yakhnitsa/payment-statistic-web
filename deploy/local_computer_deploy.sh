#!/usr/bin/env bash
delimiter='-------------------------------------------------------------'


echo -e $delimiter\\n\\t'Starting script:'$0

echo -e $delimiter\\n\\t'webpack build payments.js'

    npm run build

echo -e $delimiter\\n\\t'maven build project'

    mvn install -DskipTests

echo -e $delimiter\\n\\t'Terminate old process'

    wmic PROCESS where "name like '%java.exe%' AND CommandLine like '%payment-statistic-web%'" Call Terminate

echo -e $delimiter\\n\\t'Start jar file'

    nohup java -Dfile.encoding=UTF8 -jar target/payment-statistic-web-0.0.1-SNAPSHOT.jar > logfile.txt \
    --spring.profiles.active=dev \
    --mail_password=$1 \
    --backup_dir=D:\\Users\\Yuriy\\Dropbox\\payments_backup &


echo -e $delimiter\\n\\t'Restart is done!'