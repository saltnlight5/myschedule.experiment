#!/usr/bin/bash
DIR=$(dirname $0)
$DIR/run-java quartz.experiment.QuartzServerClean -Dconfig=$1 "$@"