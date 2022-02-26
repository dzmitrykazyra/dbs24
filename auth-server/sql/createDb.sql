sudo su postgres
-- createuser tkn_admin
--psql -c "initdb --locale en_US.UTF-8 -D '/var/lib/postgres/data'"
psql -c "CREATE DATABASE tkn_cards_dev"
psql -c "CREATE ROLE tkn_admin  WITH LOGIN CREATEDB ENCRYPTED PASSWORD '$tkn$m0n1t0ringpwd'"
psql -c "ALTER USER tkn_admin WITH PASSWORD '$tkn$0ss93r$'"
psql -c "grant all privileges on database tkn_cards_dev to tkn_admin"
-- execute on dg
psql -c "CREATE SCHEMA tkn_cards_schema"
-- execute on dg
psql -c "SET search_path TO tkn_cards_schema"
-- execute on dg
psql -c "GRANT USAGE ON SCHEMA tkn_cards_schema TO tkn_admin"
-- execute on dg
psql -c "GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA tkn_cards_schema  TO tkn_admin"
psql -c "ALTER database tkn_cards_dev SET search_path TO tkn_cards_schema"

sudo ufw allow 5432/tcp