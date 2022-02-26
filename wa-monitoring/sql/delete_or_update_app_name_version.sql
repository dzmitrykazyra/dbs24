ALTER TABLE wa_users_devices ADD COLUMN app_name TStr100;
ALTER TABLE wa_users_devices ADD COLUMN app_version TStr100;
ALTER TABLE wa_users_devices_android DROP COLUMN app_name;
ALTER TABLE wa_users_devices_android DROP COLUMN app_version;