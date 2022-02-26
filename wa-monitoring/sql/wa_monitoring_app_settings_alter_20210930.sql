drop table wa_package_details;

alter table wa_app_settings  add column company_name tstr100;
alter table wa_app_settings_hist  add column company_name tstr100;
alter table wa_app_settings  add column app_name tstr100;
alter table wa_app_settings_hist  add column app_name tstr100;
alter table wa_app_settings  add column site_url tstr100;
alter table wa_app_settings_hist  add column site_url tstr100;
alter table wa_app_settings  add column whatsapp_id tstr100;
alter table wa_app_settings_hist  add column whatsapp_id tstr100;

update wa_app_settings set company_name = 'not defined' where company_name is null;
update wa_app_settings_hist set company_name = 'not defined' where company_name is null;
update wa_app_settings set app_name = 'not defined' where app_name is null;
update wa_app_settings_hist set app_name = 'not defined' where app_name is null;
update wa_app_settings set site_url = 'not defined' where site_url is null;
update wa_app_settings_hist set site_url = 'not defined' where site_url is null;
update wa_app_settings set whatsapp_id = 'not defined' where whatsapp_id is null;
update wa_app_settings_hist set whatsapp_id = 'not defined' where whatsapp_id is null;

commit;

ALTER TABLE wa_app_settings ALTER COLUMN company_name SET NOT NULL;
ALTER TABLE wa_app_settings ALTER COLUMN app_name SET NOT NULL;