alter table wa_users_devices_ios add column apple_id TStr100;
alter table wa_users_devices_ios_hist add column apple_id TStr100;

update wa_users_devices_ios set apple_id = device_id;

ALTER TABLE wa_users_devices_ios ALTER COLUMN apple_id DROP NOT NULL;
ALTER TABLE wa_users_devices_ios ADD CONSTRAINT AK_KEY_UD_IOS unique (apple_id);