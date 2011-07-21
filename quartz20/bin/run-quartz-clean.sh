#!/usr/bin/bash
DIR=$(dirname $0)
$DIR/run-java quartz.experiment.QuartzServer2 -Dconfig=$1 "$@"