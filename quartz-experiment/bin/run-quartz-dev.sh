#!/usr/bin/bash
RUN_JAVA_CP="$RUN_JAVA_CP;C:\projects\quartz-trunk\quartz\target\test-classes"
RUN_JAVA_CP="$RUN_JAVA_CP;C:\projects\quartz-trunk\quartz\target\classes"
RUN_JAVA_CP="$RUN_JAVA_CP;C:\projects\quartz-trunk\quartz\target\dependency\*"
RUN_JAVA_CP="$RUN_JAVA_CP;C:\projects\quartz-trunk\quartz-oracle\target\test-classes"
RUN_JAVA_CP="$RUN_JAVA_CP;C:\projects\quartz-trunk\quartz-oracle\target\classes"
RUN_JAVA_CP="$RUN_JAVA_CP;C:\projects\quartz-trunk\quartz-oracle\target\dependency\*"
export RUN_JAVA_CP
DIR=$(dirname $0)
$DIR/run-java quartz.experiment.QuartzServer -Dconfig=$1