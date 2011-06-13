mysql> CREATE DATABASE quartz;
mysql> CREATE USER 'quartz'@'localhost' IDENTIFIED BY 'quartz123';
mysql> GRANT ALL PRIVILEGES ON *.* TO 'quartz'@'localhost';
mysql> FLUSH PRIVILEGES;

$ mysql -u quartz -p quartz < config/tables_mysql.sql