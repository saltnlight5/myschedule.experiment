Example runs:

# Runnig quick simple quartz scheduler
bin/run-quartz.sh

# Running RMI examples
bin/run-java quartz.experiment.QuartzServer -Dconfig=config/quartz.properties.rmi-server
bin/run-java quartz.experiment.QuartzClient -Dconfig=config/quartz.properties.rmi-client -DloadJobsFilename=config/quartz-jobs.xml

# Runninng spring examples
bin/run-java quartz.experiment.spring.SpringServer config/spring-plain-quartz-server-beans.xml -Dconfig=config/quartz.properties.rmi-server
bin/run-java quartz.experiment.spring.SpringServer config/spring-plain-quartz-client-beans.xml -Dwait=false -Dmethod=showTriggerNames

# Running a standalone database supported quartz
bin/run-spring.sh config/spring/spring-quartz-with-datasource.xml

# Running a clustered examples
# On terminal#1
$ sqlplus quartz/quartz123 < config/tables_oracle.sql
$ bin/run-spring.sh -Dconfig=config/quartz.properties.database_clustered config/spring/spring-quartz-with-datasource.xml
# On terminal#2
$ bin/run-spring.sh -Dconfig=config/quartz.properties.database_clustered config/spring/spring-quartz-with-scheduling-jobs.xml 

== BUGS ==
4:38 PM 06/05/2011
When starting quartz that failed a plugin, why the exception (validation failed) only logged but doesn't throw to scheduler.start()?
Eg: use a wrong jobs file in the following
bin/run-quartz.sh config/quartz.properties.load-jobs

== First time Maven setup ==
You will need the Oracle XE jdbc driver.
1. Download it from http://www.oracle.com/technetwork/database/features/jdbc/index-091264.html
2. Install it to your local maven repository using this command:
	$ mvn install:install-file -Dpackaging=jar -DgroupId=com.oracle -DartifactId=ojdbc14 -Dversion=10.2.0.1.0.XE -Dfile="ojdbc14.jar"