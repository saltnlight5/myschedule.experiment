NOTE: 

This project depends on myschedule-quartz-extra that's not available in maven central.
You can either download the jar from http://code.google.com/p/myschedule/downloads/list, and install it to your 
local maven repository. Or you may checkout the source and run 'mvn install'.

I use latest quartz SNAPSHOT in pom too, so you need to do the same as above. But if you are not experimenting the 
latest SNAPSHOT, you may switch to a stable version, which should be available in maven central already. 

To get started with In-Memory Quartz
====================================
1. Run: mvn dependency:copy-dependencies
2. Run scripts/run-java myschedule.quartz.extra.SchedulerMain scripts/quartz.properties

To get started with Database Quartz
===================================
1. Pick your database (eg: mysql)
   1.a Create a user: quartz, password: quartz123
   1.b Create the quartz schema (SQL script is under Quartz's distribution pkg: docs/dbTables/table_<db>.sql)
2. Run: mvn dependency:copy-dependencies
3. Run scripts/run-java myschedule.quartz.extra.SchedulerMain scripts/quartz.properties.database
