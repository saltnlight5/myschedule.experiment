Running myschedule.war in JBoss5

* Due to JBoss server package slf4j-1.5.5, and it's conflict with quartz-1.8.x's slf4j-1.6.1. We may deploy
  myschedule.war in two ways:
  
Option#1
1. Copy all jboss5-server-config-set/* into JBOSS_HOME/server/default
2. We must remove JBOSS_HOME/common/lib/slf4j-*.jar files first because these
will conflict with what's package in myschedule.war or (quartz-1.8+)
4. Copy myschedule into JBOSS_HOME/server/default/deploy/quartz-demo
3. Build myschedule-experiment/jboss6-quartz jar and copy into JBOSS_HOME/server/default/deploy/quartz-demo

Option#2
1. Copy all jboss5-server-config-set/* into JBOSS_HOME/server/default
2. Remove the following from myschedule.war and repackage it.
   slf4j-*.jar and log4j-*.jar and jcl-over-slf4j-*.jar
   jta-*.jar
3. Copy new myschedule.war into JBOSS_HOME/server/default/deploy/quartz-demo
4. Build myschedule-experiment/jboss6-quartz jar and copy into JBOSS_HOME/server/default/deploy/quartz-demo