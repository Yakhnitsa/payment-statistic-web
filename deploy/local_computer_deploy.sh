#!/usr/bin/env bash
delimiter='-------------------------------------------------------------'


echo -e $delimiter\\n\\t'Starting script:'$0

#echo -e $delimiter\\n\\t'webpack build main.js'

    #npm run build

#echo -e $delimiter\\n\\t'maven build project'


    #mvn install -DskipTests

echo -e $delimiter\\n\\t'Start jar file'

    java -Dfile.encoding=UTF8 -jar target/payment-statistic-web-0.0.1-SNAPSHOT.jar \
    --spring.profiles.active=dev \
    --mail_password=$1 \
    --backup_dir=D:\\Users\\Yuriy\\Dropbox\\payments_backup
