How To deploy and run Custom QuartzScheduler service in JBoss6.
Method#1:
1. Copy and overwrite everything in this dir (jboss6-server-config-set) into JBOSS_HOME/server/default
2. Copy mysql jdbc.jar into JBOSS_HOME/server/default/lib
3. Start jboss6 server
4. Copy all target/dependency/* into JBOSS_HOME/server/default/deploy/quartz-demo.last
	c3p0-0.9.1.2.jar
	commons-dbcp-1.4.jar
	commons-pool-1.5.4.jar
	quartz-1.8.5.jar
	slf4j-api-1.6.1.jar
	slf4j-log4j12-1.6.1.jar
5. Build this project jar (jboss6-quartz-examples-0.0.1-SNAPSHOT.jar)
6. Copy target/jboss6-quartz-examples-0.0.1-SNAPSHOT.jar into JBOSS_HOME/server/default/deploy/quartz-demo.last

Method#2:
1. Copy and overwrite everything in this dir (jboss6-server-config-set) into JBOSS_HOME/server/default
2. Remove JBOSS_HOME/server/default/deploy/quartz-demo.last/my-quartz-scheduler-jboss-beans.xml
3. Remove JBOSS_HOME/server/default/deploy/quartz-ra.rar
4. Start jboss6 server
5. Build this project jar (jboss6-quartz-examples-0.0.1-SNAPSHOT.jar)
6. Unpack myschedule-1.4.4.war and add jboss6-quartz-examples-0.0.1-SNAPSHOT.jar into WEB-INF/lib
7. Deploy new myschedule-1.4.4-m.war JBOSS_HOME/server/default/deploy
