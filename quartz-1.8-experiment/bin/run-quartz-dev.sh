#!/usr/bin/bash
RUN_JAVA_CP="$RUN_JAVA_CP;C:\projects\quartz-1.8.x-branch\quartz\target\test-classes"
RUN_JAVA_CP="$RUN_JAVA_CP;C:\projects\quartz-1.8.x-branch\quartz\target\classes"
RUN_JAVA_CP="$RUN_JAVA_CP;C:\projects\quartz-1.8.x-branch\quartz\target\dependency\*"
RUN_JAVA_CP="$RUN_JAVA_CP;C:\projects\quartz-1.8.x-branch\quartz-oracle\target\test-classes"
RUN_JAVA_CP="$RUN_JAVA_CP;C:\projects\quartz-1.8.x-branch\quartz-oracle\target\classes"
RUN_JAVA_CP="$RUN_JAVA_CP;C:\projects\quartz-1.8.x-branch\quartz-oracle\target\dependency\*"
export RUN_JAVA_CP
DIR=$(dirname $0)
$DIR/run-java quartz.experiment.QuartzServer -Dconfig=$1