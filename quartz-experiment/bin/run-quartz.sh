#!/usr/bin/bash
DIR=$(dirname $0)
QUARTZ_CONFIG=$1
shift
$DIR/run-spring.sh $DIR/../config/spring/spring-quartz.xml -Dconfig=$QUARTZ_CONFIG "$@"