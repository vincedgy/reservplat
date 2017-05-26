#!/bin/bash
#set -x

SEQUENCE="\
config-service \
eureka-service \
zipkin-service \
hystrix-dashboard \
reservation-service \
reservation-client"

rm pids.txt

for MODULE in $SEQUENCE
do
    JARMODULE=$(ls ./$MODULE*.jar)
    echo "Launching module $JARMODULE..."
    
    java -jar $JARMODULE > $JARMODULE.log 2>&1 & 
    
    CHILD_PID=$!
    echo $CHILD_PID >> pids.txt
    echo "Running $JARMODULE on Process $CHILD_PID"
    sleep 10
done