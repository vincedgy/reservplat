#!/bin/bash
set -x
BASEDIR=$(pwd)
mkdir distrib

for PROJECT in $(find . -name 'pom.xml' -exec dirname {} \;)
do
    cd $PROJECT
    echo "Build project from $(pwd)"
    PACKAGE=$(mvn clean package -DskipTests spring-boot:repackage | grep 'Building jar:' | cut -f4 -d' ')
    if [ ! -z $PACKAGE ]; then
        echo "$PACKAGE is built"
        mv $PACKAGE $BASEDIR/distrib
    else
        echo "Ouch can't find any package built for the project"
    fi
    cd ..
done