Example runs:
bin/run-java quartz.experiment.spring.SpringServer config/spring-plain-quartz-server-beans.xml -Dconfig=config/quartz.properties.rmi-server
bin/run-java quartz.experiment.spring.SpringServer config/spring-plain-quartz-client-beans.xml -Dwait=false -Dmethod=showTriggerNames

= Databases =

== MySQL JDBC ==
URL format: jdbc:mysql://<servername>:3306/<databaseName>[?<prop>=<value>&<prop>=<value>]
Example: jdbc:mysql://localhost:3306/quartz

== Oracle JDBC ==
URL format: jdbc:oracle:thin:@<servername>:1521:<SID>
Example: jdbc:oracle:thin:@localhost:1521:XE

== MSSQL JDBC ==
URL format: jdbc:sqlserver://<servername>:1433;<databaseName>[;property=value[;property=value]]
Example: jdbc:microsoft:sqlserver://localhost:1433;QUARTZ
Doc: http://msdn.microsoft.com/en-us/library/ms378428%28v=sql.90%29.aspx

== PostgresSQL JDBC ==
URL format: jdbc:postgresql://<servername>:5432/databaseName[?<prop>=<value>&<prop>=<value>]
Example: jdbc:postgresql://localhost:5432/quartz