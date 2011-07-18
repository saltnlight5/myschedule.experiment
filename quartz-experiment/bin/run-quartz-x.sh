#!/usr/bin/bash
DIR=$(dirname $0)
NAME=$1
shift
$DIR/run-java quartz.experiment.$NAME -Dconfig=$1 "$@"