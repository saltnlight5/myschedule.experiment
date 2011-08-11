Running myschedule.war in JBoss6

1. Copy all jboss6-server-config-set/* into JBOSS_HOME/server/default
2. Remove the following from myschedule.war and repackage it.
   slf4j-*.jar and log4j-*.jar and jcl-over-slf4j-*.jar
   jta-*.jar
3. Copy new myschedule.war into JBOSS_HOME/server/default/deploy/quartz-demo
4. Build myschedule-experiment/jboss6-quartz jar and copy into JBOSS_HOME/server/default/deploy/quartz-demo
