CREATE SCHEMA my_schema;
-- To change search_path on a database-level
ALTER database "my_database" SET search_path TO my_schema;

CREATE SCHEMA dbs24_dev_schema;
ALTER database "dbs24_dev" SET search_path TO dbs24_dev_schema;