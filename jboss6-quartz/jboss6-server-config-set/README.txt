How To deploy and run Custom QuartzScheduler service in JBoss6.

1. Copy mysql jdbc.jar into lib
2. Copy all this project dep libs into deploy/quartz-demo
	c3p0-0.9.1.2.jar
	commons-dbcp-1.4.jar
	commons-pool-1.5.4.jar
	quartz-1.8.5.jar
	slf4j-api-1.6.1.jar
	slf4j-log4j12-1.6.1.jar
4. Build this project jar (jboss6-quartz-examples-0.0.1-SNAPSHOT.jar)
5. Copy target/jboss6-quartz-examples-0.0.1-SNAPSHOT.jar into deploy/quartz-demo
6. Copy and overwrite everything in this dir into JBOSS_HOME/server/default

