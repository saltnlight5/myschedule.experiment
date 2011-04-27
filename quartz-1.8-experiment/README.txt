Example runs:
bin/run-java quartz.experiment.QuartzServer -Dconfig=config/quartz.properties.rmi-server
bin/run-java quartz.experiment.QuartzServer -Dconfig=config/quartz.properties.rmi-client -DloadJobsFilename=config/quartz-jobs.xml

bin/run-java quartz.experiment.spring.SpringServer config/spring-plain-quartz-beans.xml
bin/run-java quartz.experiment.spring.SpringServer config/spring-plain-quartz-beans.xml -Dconfig=config/quartz.properties.rmi-server
