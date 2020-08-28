#!/usr/bin/env bash
delimiter='-------------------------------------------------------------'

echo -e $delimiter\\n\\t'Starting script:'$0

echo -e $delimiter\\n\\t'webpack build main.js'

    npm run build

echo -e $delimiter\\n\\t'maven build project'

    mvn install -DskipTests

echo -e $delimiter\\n\\t'Copy files to server...'

    scp -P822 target/payment-statistic-web-0.0.1-SNAPSHOT.jar  \
    yri@myapp.ukrtranslogistic.com.ua:~/utl-web-app

echo -e $delimiter\\n\\t'Restart server'
ssh -p 822 yri@myapp.ukrtranslogistic.com.ua << EOF
    lsof -t -i:8443 | xargs kill -9
    nohup java -Dfile.encoding=UTF8 -jar utl-web-app/payment-statistic-web-0.0.1-SNAPSHOT.jar > logfile.txt \
    --MAIL_PASSWORD=$1 \
    --MYSQL_PASSWORD=$2 &
EOF

echo -e $delimiter\\n\\t'Done'
