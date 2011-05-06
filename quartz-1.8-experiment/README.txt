Example runs:
bin/run-java quartz.experiment.QuartzServer -Dconfig=config/quartz.properties.rmi-server
bin/run-java quartz.experiment.QuartzClient -Dconfig=config/quartz.properties.rmi-client -DloadJobsFilename=config/quartz-jobs.xml

bin/run-java quartz.experiment.spring.SpringServer config/spring-plain-quartz-server-beans.xml -Dconfig=config/quartz.properties.rmi-server
bin/run-java quartz.experiment.spring.SpringServer config/spring-plain-quartz-client-beans.xml -Dwait=false -Dmethod=showTriggerNames
