#!/usr/bin/bash
#
# The -Xrunjdwp will remote JVM debugging.
# The -Dconfig is for QuartzServer that take a classpath resource or relative to CWD.
# The -Djcl.isolateLogging=false is for JarClassLoader to not use isolated log4j, which messup quartz logging.
#
# User may use -DjarPaths to specify a comma separated classpath to load by JarClassLoader.
#

DIR=$(dirname $0)
$DIR/run-java "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000"\
 -Dconfig=$1\
 -Djcl.isolateLogging=false\
 quartz.experiment.QuartzServer "$@"
