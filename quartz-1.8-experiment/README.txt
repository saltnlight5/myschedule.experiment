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