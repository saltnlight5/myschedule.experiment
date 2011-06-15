CREATE DATABASE quartz18;
CREATE USER 'quartz18'@'localhost' IDENTIFIED BY 'quartz18123';
GRANT ALL PRIVILEGES ON *.* TO 'quartz18'@'localhost';
FLUSH PRIVILEGES;

-- $ mysql -u quartz18 -p quartz18 < config/sql/tables_mysql.sql