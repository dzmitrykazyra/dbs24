sudo su postgres
--psql -c "initdb --locale en_US.UTF-8 -D '/var/lib/postgres/data'"
psql -c "CREATE ROLE ml_admin  WITH LOGIN CREATEDB ENCRYPTED PASSWORD '$ml9m0$pwd76'"
psql -c "CREATE DATABASE mailing_letters_dev OWNER ml_admin"
--psql -c "ALTER USER ml_admin WITH PASSWORD '$ml9m0$pwd76'"
psql -c "grant all privileges on database mailing_letters_dev to ml_admin"
psql -c "CREATE SCHEMA mailing_letters_schema_dev"
psql -c "SET search_path TO mailing_letters_schema_dev"
psql -c "GRANT USAGE ON SCHEMA mailing_letters_schema_dev TO ml_admin"
psql -c "GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA mailing_letters_schema_dev  TO ml_admin"
psql -c "ALTER database mailing_letters_dev SET search_path TO mailing_letters_schema_dev"

sudo ufw allow 5432/tcp