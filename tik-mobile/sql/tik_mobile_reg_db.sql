sudo su postgres
--psql -c "initdb --locale en_US.UTF-8 -D '/var/lib/postgres/data'"
psql -c "CREATE ROLE tkm_admin  WITH LOGIN CREATEDB ENCRYPTED PASSWORD '$tk0m0$pwd76'"
psql -c "CREATE DATABASE tkm_mobile_dev OWNER tkm_admin"
--psql -c "ALTER USER tkm_admin WITH PASSWORD '$tk0m0$pwd76'"
psql -c "grant all privileges on database tkm_mobile_dev to tkm_admin"
psql -c "CREATE SCHEMA tkm_mobile_schema_dev"
psql -c "SET search_path TO tkm_mobile_schema_dev"
psql -c "GRANT USAGE ON SCHEMA tkm_mobile_schema_dev TO tkm_admin"
psql -c "GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA tkm_mobile_schema_dev  TO tkm_admin"
psql -c "ALTER database tkm_mobile_dev SET search_path TO tkm_mobile_schema_dev"

sudo ufw allow 5432/tcp