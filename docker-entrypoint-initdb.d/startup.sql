DROP USER IF EXISTS 'workshop_rwx';
CREATE USER 'workshop_rwx'@'%' IDENTIFIED BY 'jk9';

CREATE DATABASE IF NOT EXISTS main;
GRANT SELECT, INSERT, DELETE, UPDATE ON main.* TO 'workshop_rwx'@'%';
GRANT ALL ON main.* TO 'workshop_rwx'@'%';
