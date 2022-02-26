
1st and important step is connect to your db:

psql -d yourDBName

2 step, grant privileges

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO userName;


sudo su postgres
-- createuser tik_admin
--psql -c "initdb --locale en_US.UTF-8 -D '/var/lib/postgres/data'"
psql -c "CREATE DATABASE tik_assist_dev"
psql -c "CREATE ROLE tik_admin  WITH LOGIN CREATEDB ENCRYPTED PASSWORD '$tik$m0n1t0ringpwd'"
psql -c "CREATE ROLE tik_external  WITH LOGIN CREATEDB ENCRYPTED PASSWORD '$tik$e0x1t0e2r9nal'"
psql -c "ALTER USER tik_admin WITH PASSWORD '$tik$d9v3l1pm0nt$'"
psql -c "grant all privileges on database tik_assist_dev to tik_admin"
psql -c "CREATE SCHEMA tik_assist_schema"
psql -c "SET search_path TO tik_assist_schema"
psql -c "GRANT USAGE ON SCHEMA tik_assist_schema TO tik_admin"
psql -c "GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA tik_assist_schema  TO tik_admin"
psql -c "ALTER DEFAULT PRIVILEGES IN SCHEMA tik_assist_schema GRANT ALL ON TABLES TO tik_external"
psql -c "GRANT USAGE ON SCHEMA tik_assist_schema TO tik_external"
psql -c "GRANT ALL ON SCHEMA tik_assist_schema TO tik_external"
psql -c "GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA tik_assist_schema TO tik_external"
psql -c "GRANT SELECT ON ALL TABLES IN SCHEMA tik_assist_schema TO tik_external"
psql -c "grant all on all sequences in schema tik_assist_schema to tik_external"
psql -c "ALTER database tik_assist_dev SET search_path TO tik_assist_schema"
psql -c "ALTER DEFAULT PRIVILEGES  FOR ROLE tik_external IN SCHEMA tik_assist_schema GRANT SELECT ON TABLES TO tik_external"



sudo ufw allow 5432/tcp