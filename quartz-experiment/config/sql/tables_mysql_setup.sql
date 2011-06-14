CREATE DATABASE quartz2;
CREATE USER 'quartz2'@'localhost' IDENTIFIED BY 'quartz2123';
GRANT ALL PRIVILEGES ON *.* TO 'quartz2'@'localhost';
FLUSH PRIVILEGES;
-- $ mysql -u quartz2 -p quartz2 < config/sql/tables_mysql.sql