CREATE DATABASE vcs
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Russian_Russia.1251'
    LC_CTYPE = 'Russian_Russia.1251'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

CREATE SCHEMA vcs_dev;
-- To change search_path on a database-level
ALTER database "vcs" SET search_path TO vcs_dev;