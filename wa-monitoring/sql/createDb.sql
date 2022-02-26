sudo su postgres
-- createuser wa_admin
--psql -c "initdb --locale en_US.UTF-8 -D '/var/lib/postgres/data'"
psql -c "CREATE DATABASE wa_monitoring_dev"
psql -c "CREATE ROLE wa_admin  WITH LOGIN CREATEDB ENCRYPTED PASSWORD '$wa$m0n1t0ringpwd'"
psql -c "ALTER USER wa_admin WITH PASSWORD '$wa$m1n0t1r2ng$'"
psql -c "grant all privileges on database wa_monitoring_dev to wa_admin"
psql -c "CREATE SCHEMA wa_schema_dev"
psql -c "SET search_path TO wa_schema_dev"
psql -c "GRANT USAGE ON SCHEMA wa_schema_dev TO wa_admin"
psql -c "GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA wa_schema_dev  TO wa_admin"
psql -c "ALTER database wa_monitoring_dev SET search_path TO wa_schema_dev"

sudo ufw allow 5432/tcp