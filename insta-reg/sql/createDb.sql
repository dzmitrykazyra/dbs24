sudo su postgres
-- createuser insta_admin
psql -c "CREATE DATABASE insta_reg_dev"
psql -c "CREATE ROLE insta_admin  WITH LOGIN CREATEDB ENCRYPTED PASSWORD '$insta$m0n1t0ringpwd'"
psql -c "ALTER USER insta_admin WITH PASSWORD '$insta$m0n1t0ringpwd'"
psql -c "grant all privileges on database insta_reg_dev to insta_admin"
psql -c "CREATE SCHEMA insta_reg_schema"
psql -c "GRANT USAGE ON SCHEMA insta_reg_schema TO insta_admin"
psql -c "GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA insta_reg_schema  TO insta_admin"
psql -c "ALTER database insta_reg_dev SET search_path TO insta_reg_schema"
sudo ufw allow 5432/tcp