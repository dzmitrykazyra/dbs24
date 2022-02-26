su postgres

1st and important step is connect to your db:

psql -d yourDBName

2 step, grant privileges

--GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA wa_schema TO wa_support;

-- READ_ONLY ACCESS
psql -c "CREATE ROLE wa_support  WITH LOGIN CREATEDB ENCRYPTED PASSWORD '$wa$e0x1t2e3r4nal'"
psql -c "GRANT USAGE ON SCHEMA wa_schema TO wa_support"
psql -c "GRANT SELECT ON ALL TABLES IN SCHEMA wa_schema TO wa_support"
--psql -c "ALTER DEFAULT PRIVILEGES IN SCHEMA wa_schema GRANT ALL ON TABLES TO wa_support"

--psql -c "GRANT ALL ON SCHEMA wa_schema TO wa_support"
--psql -c "GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA wa_schema TO wa_support"
--psql -c "grant all on all sequences in schema wa_schema to wa_support"
--psql -c "ALTER DEFAULT PRIVILEGES FOR ROLE wa_support IN SCHEMA wa_schema GRANT SELECT ON TABLES TO wa_support"