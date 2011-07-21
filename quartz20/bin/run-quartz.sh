#!/usr/bin/bash
DIR=$(dirname $0)
$DIR/run-java quartz.experiment.QuartzServer -Dconfig=$1 "$@"