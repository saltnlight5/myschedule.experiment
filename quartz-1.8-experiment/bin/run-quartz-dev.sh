#!/usr/bin/bash
JAVA_CP="$JAVA_CP;C:\projects\quartz-1.8.x-branch\quartz\target\test-classes"
JAVA_CP="$JAVA_CP;C:\projects\quartz-1.8.x-branch\quartz\target\classes"
JAVA_CP="$JAVA_CP;C:\projects\quartz-1.8.x-branch\quartz\target\dependency\*"
JAVA_CP="$JAVA_CP;C:\projects\quartz-1.8.x-branch\quartz-oracle\target\test-classes"
JAVA_CP="$JAVA_CP;C:\projects\quartz-1.8.x-branch\quartz-oracle\target\classes"
JAVA_CP="$JAVA_CP;C:\projects\quartz-1.8.x-branch\quartz-oracle\target\dependency\*"
export JAVA_CP
DIR=$(dirname $0)
$DIR/run-java quartz.experiment.QuartzServer -Dconfig=$1