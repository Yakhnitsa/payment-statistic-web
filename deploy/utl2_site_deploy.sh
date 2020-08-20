#!/usr/bin/env bash
delimiter='-------------------------------------------------------------'
echo 'Starting script:' $0
echo $delimiter
echo 'webpack build main.js'
npm run build
echo $delimiter
echo 'maven build project'
echo $delimiter
echo 'Copy files to server'
echo $delimiter
echo 'Kill old java process'
echo $delimiter
echo 'Start jar file'
