sudo su postgres
-- createuser ref_book_admin
--psql -c "initdb --locale en_US.UTF-8 -D '/var/lib/postgres/data'"
psql -c "CREATE ROLE ref_book_admin  WITH LOGIN CREATEDB ENCRYPTED PASSWORD '\$rba\$m0n1t0ringpwd'"
psql -c "CREATE DATABASE ref_book_stage OWNER ref_book_admin"
--psql -c "ALTER USER ref_book_admin WITH PASSWORD '\$rba\$m0n1t0ringpwd'"
psql -c "grant all privileges on database ref_book_stage to ref_book_admin"
-- execute on dg
psql -c "CREATE SCHEMA ref_book_schema_stage"
-- execute on dg
psql -c "SET search_path TO ref_book_schema_stage"
-- execute on dg
psql -c "GRANT USAGE ON SCHEMA ref_book_schema_stage TO ref_book_admin"
-- execute on dg
psql -c "GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA ref_book_schema_stage  TO ref_book_admin"
psql -c "ALTER database ref_book_stage SET search_path TO ref_book_schema_stage"

sudo ufw allow 5432/tcp