sudo su postgres
-- createuser wa_admin
--psql -c "initdb --locale en_US.UTF-8 -D '/var/lib/postgres/data'"
psql -c "CREATE ROLE wa_admin  WITH LOGIN CREATEDB ENCRYPTED PASSWORD '$wa$m0n1t0ringpwd'"
psql -c "DROP DATABASE wa_monitoring_stage"
psql -c "CREATE DATABASE wa_monitoring_stage OWNER wa_admin"
psql -c "ALTER USER wa_admin WITH PASSWORD '$wa$m1n0t1r2ng$'"
psql -c "grant all privileges on database wa_monitoring_stage to wa_admin"
psql -c "CREATE SCHEMA wa_schema_stage"
psql -c "SET search_path TO wa_schema_stage"
psql -c "GRANT USAGE ON SCHEMA wa_schema_stage TO wa_admin"
psql -c "GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA wa_schema_stage  TO wa_admin"
psql -c "ALTER database wa_monitoring_stage SET search_path TO wa_schema_stage"
-- psql -U postgres -d wa_monitoring_stage"
psql -c "CREATE EXTENSION pg_stat_statements"
psql -c "GRANT SELECT ON pg_stat_statements TO wa_admin"


sudo ufw allow 5432/tcp