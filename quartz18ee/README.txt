>>> JBoss7 notes
When creating your own module, the directory structure must match to the "name"
Eg: 
$ find /c/apps/jboss-as-7.0.0.Final/modules/org/quartz-scheduler/
/c/apps/jboss-as-7.0.0.Final/modules/org/quartz-scheduler/
/c/apps/jboss-as-7.0.0.Final/modules/org/quartz-scheduler/main
/c/apps/jboss-as-7.0.0.Final/modules/org/quartz-scheduler/main/module.xml
/c/apps/jboss-as-7.0.0.Final/modules/org/quartz-scheduler/main/quartz-1.8.5.jar
/c/apps/jboss-as-7.0.0.Final/modules/org/quartz-scheduler/main/quartz-1.8.5.jar.index

$ cat /c/apps/jboss-as-7.0.0.Final/modules/org/quartz-scheduler/main/module.xml
<module xmlns="urn:jboss:module:1.0" name="org.quartz-scheduler">
    <resources>
        <resource-root path="quartz-1.8.5.jar"/>
    </resources>

    <dependencies>
        <module name="org.slf4j"/>
    </dependencies>
</module>

Quartz1.8
>>> When -Dorg.quartz.properties file is not found, you will get this stacktrace:
23:08:44,765 ERROR [STDERR] org.quartz.SchedulerException: Properties file: 'quartz2.proeprties' could not be found.
23:08:44,781 ERROR [STDERR]     at org.quartz.impl.StdSchedulerFactory.initialize(StdSchedulerFactory.java:375)
23:08:44,781 ERROR [STDERR]     at org.quartz.impl.StdSchedulerFactory.getScheduler(StdSchedulerFactory.java:1445)

JBoss6
$ JAVA_OPTS=-Dorg.quartz.properties=quartz2.properties bin/run.bat

>>> Jboss6.0.0.Final contains quartz-1.8.3 (see common/lib/quartz.jar:/build.properties)

>>> It works when add quartz.jar and q18job.jar into server/lib and q18web-provided.war into server/deploy
17:33:17,457 INFO  [QuartzScheduler] Quartz Scheduler v.1.8.5 created.
17:33:17,473 INFO  [STDOUT] 2011-07-19 17:33:17,457 INFO  Thread-2 quartz.experiment.classloader.MyClassLoaderDebuggerPlugin:20| Current Thread: Thread[Thread-2,5,jboss]
17:33:17,473 INFO  [STDOUT] 2011-07-19 17:33:17,473 INFO  Thread-2 quartz.experiment.classloader.MyClassLoaderDebuggerPlugin:21| Current Thread ContextClassLoader: org.jboss.web.tomcat.service.WebCtxLoader$ENCLoader@182363
17:33:17,473 INFO  [STDOUT] 2011-07-19 17:33:17,473 INFO  Thread-2 quartz.experiment.classloader.MyClassLoaderDebuggerPlugin:24| Scheduler class: class org.quartz.impl.StdScheduler
17:33:17,473 INFO  [STDOUT] 2011-07-19 17:33:17,473 INFO  Thread-2 quartz.experiment.classloader.MyClassLoaderDebuggerPlugin:25| Scheduler class location: vfs:/C:/apps/jboss-6.0.0.Final/server/default/lib/quartz-1.8.5.jar/
17:33:17,473 INFO  [STDOUT] 2011-07-19 17:33:17,473 INFO  Thread-2 quartz.experiment.classloader.MyClassLoaderDebuggerPlugin:26| Scheduler class loader: BaseClassLoader@10469e8{vfs:///C:/apps/jboss-6.0.0.Final/server/default/conf/jboss-service.xml}
17:33:17,473 INFO  [STDOUT] 2011-07-19 17:33:17,473 INFO  Thread-2 quartz.experiment.classloader.MyClassLoaderDebuggerPlugin:33| Before loading job class.
17:33:17,473 INFO  [STDOUT] 2011-07-19 17:33:17,473 INFO  Thread-2 quartz.experiment.classloader.MyClassLoaderDebuggerPlugin:34| CascadingClassLoadHelperExt getBestCandidate null
17:33:17,473 INFO  [STDOUT] 2011-07-19 17:33:17,473 INFO  Thread-2 quartz.experiment.classloader.MyClassLoaderDebuggerPlugin:35| CascadingClassLoadHelperExt getClassLoader org.jboss.web.tomcat.service.WebCtxLoader$ENCLoader@182363
17:33:17,489 INFO  [STDOUT] 2011-07-19 17:33:17,489 INFO  Thread-2 quartz.experiment.classloader.MyClassLoaderDebuggerPlugin:38| After loading job class.
17:33:17,489 INFO  [STDOUT] 2011-07-19 17:33:17,489 INFO  Thread-2 quartz.experiment.classloader.MyClassLoaderDebuggerPlugin:39| CascadingClassLoadHelperExt Found job class: class quartz.experiment.classloader.SimpleJob
17:33:17,489 INFO  [STDOUT] 2011-07-19 17:33:17,489 INFO  Thread-2 quartz.experiment.classloader.MyClassLoaderDebuggerPlugin:40| CascadingClassLoadHelperExt Found job class classloader: BaseClassLoader@10469e8{vfs:///C:/apps/jboss-6.0.0.Final/server/default/conf/jboss-service.xml}
17:33:17,489 INFO  [STDOUT] 2011-07-19 17:33:17,489 INFO  Thread-2 quartz.experiment.classloader.MyClassLoaderDebuggerPlugin:41| CascadingClassLoadHelperExt Found job class location: vfs:/C:/apps/jboss-6.0.0.Final/server/default/lib/q18job-1.0.0-SNAPSHOT.jar/
17:33:17,489 INFO  [STDOUT] 2011-07-19 17:33:17,489 INFO  Thread-2 quartz.experiment.classloader.MyClassLoaderDebuggerPlugin:42| CascadingClassLoadHelperExt getBestCandidate org.quartz.simpl.LoadingLoaderClassLoadHelper@16ff7a
17:33:17,489 INFO  [STDOUT] 2011-07-19 17:33:17,489 INFO  Thread-2 quartz.experiment.classloader.MyClassLoaderDebuggerPlugin:43| CascadingClassLoadHelperExt getClassLoader BaseClassLoader@10469e8{vfs:///C:/apps/jboss-6.0.0.Final/server/default/conf/jboss-service.xml}
17:33:17,489 INFO  [STDOUT] 2011-07-19 17:33:17,489 INFO  Thread-2 quartz.experiment.classloader.StaticClassLoaderDebugger:11| Start static initializer
17:33:17,551 INFO  [STDOUT] 2011-07-19 17:33:17,489 INFO  Thread-2 quartz.experiment.classloader.StaticClassLoaderDebugger:18| class quartz.experiment.classloader.StaticClassLoaderDebugger is loaded from url vfs:/C:/apps/jboss-6.0.0.Final/server/default/deploy/q18web-provided-1.0.0-SNAPSHOT.war/WEB-INF/classes/
17:33:17,551 INFO  [STDOUT] 2011-07-19 17:33:17,551 INFO  Thread-2 quartz.experiment.classloader.StaticClassLoaderDebugger:19| class quartz.experiment.classloader.StaticClassLoaderDebugger classloader BaseClassLoader@1f6670f{vfs:///C:/apps/jboss-6.0.0.Final/server/default/deploy/q18web-provided-1.0.0-SNAPSHOT.war}
17:33:17,551 INFO  [STDOUT] 2011-07-19 17:33:17,551 INFO  Thread-2 quartz.experiment.classloader.StaticClassLoaderDebugger:13| End static initializer
17:33:17,551 INFO  [STDOUT] 2011-07-19 17:33:17,551 INFO  Thread-2 quartz.experiment.classloader.StaticClassLoaderDebugger:18| class quartz.experiment.classloader.StaticClassLoaderDebugger is loaded from url vfs:/C:/apps/jboss-6.0.0.Final/server/default/deploy/q18web-provided-1.0.0-SNAPSHOT.war/WEB-INF/classes/
17:33:17,567 INFO  [STDOUT] 2011-07-19 17:33:17,551 INFO  Thread-2 quartz.experiment.classloader.StaticClassLoaderDebugger:19| class quartz.experiment.classloader.StaticClassLoaderDebugger classloader BaseClassLoader@1f6670f{vfs:///C:/apps/jboss-6.0.0.Final/server/default/deploy/q18web-provided-1.0.0-SNAPSHOT.war}
17:33:17,567 INFO  [STDOUT] 2011-07-19 17:33:17,567 INFO  Thread-2 quartz.experiment.classloader.MyClassLoaderDebuggerPlugin:52| quartz.experiment.classloader.MyClassLoaderDebuggerPlugin@1a17db8 myClassLoaderDebuggerPlugin initialized.
17:33:17,567 INFO  [RAMJobStore] RAMJobStore initialized.
17:33:17,567 INFO  [QuartzScheduler] Scheduler meta-data: Quartz Scheduler (v1.8.5) 'QuartzWebapp2' with instanceId 'NON_CLUSTERED'
  Scheduler class: 'org.quartz.core.QuartzScheduler' - running locally.
  NOT STARTED.
  Currently in standby mode.
  Number of jobs executed: 0
  Using thread pool 'org.quartz.simpl.SimpleThreadPool' - with 5 threads.
  Using job-store 'org.quartz.simpl.RAMJobStore' - which does not support persistence. and is not clustered.

17:33:17,567 INFO  [StdSchedulerFactory] Quartz scheduler 'QuartzWebapp2' initialized from specified file: 'quartz2.properties' in the class resource path.
17:33:17,567 INFO  [StdSchedulerFactory] Quartz scheduler version: 1.8.5
17:33:17,582 INFO  [QuartzScheduler] Scheduler QuartzWebapp2_$_NON_CLUSTERED started.
17:33:17,582 INFO  [QuartzInitializerListener] Scheduler has been started...
17:33:17,582 INFO  [QuartzInitializerListener] Storing the Quartz Scheduler Factory in the servlet context at key: org.quartz.impl.StdSchedulerFactory.KEY
17:33:17,629 INFO  [service] Removing bootstrap log handlers
17:33:17,754 INFO  [org.apache.coyote.http11.Http11Protocol] Starting Coyote HTTP/1.1 on http-127.0.0.1-8080
17:33:17,754 INFO  [org.apache.coyote.ajp.AjpProtocol] Starting Coyote AJP/1.3 on ajp-127.0.0.1-8009
17:33:17,754 INFO  [org.jboss.bootstrap.impl.base.server.AbstractServer] JBossAS [6.0.0.Final "Neo"] Started in 24s:514ms


JBoss7
$ JAVA_OPTS=-Dorg.quartz.properties=quartz2.properties bin/standalone.bat


Tomcat6
$ CATALINA_OPTS=-Dorg.quartz.properties=quartz2.properties bin/catalina.bat run

>>> case1: q18web.war without q18job.jar
2011-07-19 15:48:08,449 ERROR main quartz.experiment.classloader.MyClassLoaderDebuggerPlugin:30| Failed to find class: quartz.experiment.classloader.SimpleJob
java.lang.ClassNotFoundException: quartz.experiment.classloader.SimpleJob
        at org.apache.catalina.loader.WebappClassLoader.loadClass(WebappClassLoader.java:1680)
        at org.apache.catalina.loader.WebappClassLoader.loadClass(WebappClassLoader.java:1526)
        at org.quartz.simpl.InitThreadContextClassLoadHelper.loadClass(InitThreadContextClassLoadHelper.java:72)
        at quartz.experiment.classloader.CascadingClassLoadHelperExt.loadClass(CascadingClassLoadHelperExt.java:103)
        at quartz.experiment.classloader.MyClassLoaderDebuggerPlugin.initialize(MyClassLoaderDebuggerPlugin.java:24)
        at org.quartz.impl.StdSchedulerFactory.instantiate(StdSchedulerFactory.java:1250)
        at org.quartz.impl.StdSchedulerFactory.getScheduler(StdSchedulerFactory.java:1465)
        at org.quartz.ee.servlet.QuartzInitializerListener.contextInitialized(QuartzInitializerListener.java:152)
        at org.apache.catalina.core.StandardContext.listenerStart(StandardContext.java:4205)
        at org.apache.catalina.core.StandardContext.start(StandardContext.java:4704)
        at org.apache.catalina.core.ContainerBase.addChildInternal(ContainerBase.java:799)
        at org.apache.catalina.core.ContainerBase.addChild(ContainerBase.java:779)
        at org.apache.catalina.core.StandardHost.addChild(StandardHost.java:601)
        at org.apache.catalina.startup.HostConfig.deployWAR(HostConfig.java:943)
        at org.apache.catalina.startup.HostConfig.deployWARs(HostConfig.java:778)
        at org.apache.catalina.startup.HostConfig.deployApps(HostConfig.java:504)
        at org.apache.catalina.startup.HostConfig.start(HostConfig.java:1315)
        at org.apache.catalina.startup.HostConfig.lifecycleEvent(HostConfig.java:324)
        at org.apache.catalina.util.LifecycleSupport.fireLifecycleEvent(LifecycleSupport.java:142)
        at org.apache.catalina.core.ContainerBase.start(ContainerBase.java:1061)
        at org.apache.catalina.core.StandardHost.start(StandardHost.java:840)
        at org.apache.catalina.core.ContainerBase.start(ContainerBase.java:1053)
        at org.apache.catalina.core.StandardEngine.start(StandardEngine.java:463)
        at org.apache.catalina.core.StandardService.start(StandardService.java:525)
        at org.apache.catalina.core.StandardServer.start(StandardServer.java:754)
        at org.apache.catalina.startup.Catalina.start(Catalina.java:595)
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
        at java.lang.reflect.Method.invoke(Method.java:597)
        at org.apache.catalina.startup.Bootstrap.start(Bootstrap.java:289)
        at org.apache.catalina.startup.Bootstrap.main(Bootstrap.java:414)
		
>>> case2: q18web.war with q18job.jar in q18web.war:/WEB-INF/lib
2011-07-19 15:52:36,608 INFO  main quartz.experiment.classloader.MyClassLoaderDebuggerPlugin:25| Found job class: classquartz.experiment.classloader.SimpleJob
2011-07-19 15:52:36,608 INFO  main quartz.experiment.classloader.MyClassLoaderDebuggerPlugin:26| Found job class location: file:/C:/apps/apache-tomcat-6.0.32/webapps/q18web/WEB-INF/lib/q18job-1.0.0-SNAPSHOT.jar
2011-07-19 15:52:36,623 INFO  main quartz.experiment.classloader.MyClassLoaderDebuggerPlugin:27| CascadingClassLoadHelperExt BestCandidate org.quartz.simpl.LoadingLoaderClassLoadHelper@f9c40
2011-07-19 15:52:36,623 INFO  main quartz.experiment.classloader.MyClassLoaderDebuggerPlugin:28| CascadingClassLoadHelperExt ClassLoader WebappClassLoader
  context: /q18web
  delegate: false
  repositories:
    /WEB-INF/classes/
----------> Parent Classloader:
org.apache.catalina.loader.StandardClassLoader@6eb38a

>>> case 3: q18web.war with q18job.jar and quartz1.8.5.jar in TOMCAT_HOME/lib
2011-07-19 15:55:04,912 INFO  main quartz.experiment.classloader.MyClassLoaderDebuggerPlugin:25| Found job class: class quartz.experiment.classloader.SimpleJob
2011-07-19 15:55:04,912 INFO  main quartz.experiment.classloader.MyClassLoaderDebuggerPlugin:26| Found job class location: file:/C:/apps/apache-tomcat-6.0.32/lib/q18job-1.0.0-SNAPSHOT.jar
2011-07-19 15:55:04,912 INFO  main quartz.experiment.classloader.MyClassLoaderDebuggerPlugin:27| CascadingClassLoadHelperExt BestCandidate org.quartz.simpl.LoadingLoaderClassLoadHelper@db4fa2
2011-07-19 15:55:04,912 INFO  main quartz.experiment.classloader.MyClassLoaderDebuggerPlugin:28| CascadingClassLoadHelperExt ClassLoader WebappClassLoader
  context: /q18web
  delegate: false
  repositories:
    /WEB-INF/classes/
----------> Parent Classloader:
org.apache.catalina.loader.StandardClassLoader@cf2c80
