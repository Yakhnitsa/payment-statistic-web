#!/usr/bin/env bash
delimiter='-------------------------------------------------------------'


echo -e $delimiter\\n\\t'Starting script:'

echo -e $delimiter\\n\\t'webpack build main.js'
npm run build

echo -e $delimiter\\n\\t'maven build project'

mvn install -DskipTests

echo -e $delimiter\\n\\t'Copy files to server'
echo -e $delimiter\\n\\t'Kill old java process'
echo -e $delimiter\\n\\t'Start jar file'
