CREATE DATABASE quartz;
CREATE USER 'quartz'@'localhost' IDENTIFIED BY 'quartz123';
GRANT ALL PRIVILEGES ON *.* TO 'quartz'@'localhost';
FLUSH PRIVILEGES;

-- $ mysql -u quartz -p quartz < config/sql/tables_mysql.sql