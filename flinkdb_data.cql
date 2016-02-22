CREATE KEYSPACE flinkdb 
WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 }; 
USE flinkdb; 
CREATE TABLE data ( 
id uuid PRIMARY KEY, 
file blob, 
name ascii, 
extension ascii, 
owner_id int, 
date timestamp, 
place ascii 
);