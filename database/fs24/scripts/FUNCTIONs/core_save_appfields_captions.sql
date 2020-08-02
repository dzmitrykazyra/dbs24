DROP FUNCTION ent_save_appfieldscaptions;
CREATE OR REPLACE FUNCTION core_save_appfields_captions(p_user_id TIdBigCode, p_app_id tidcode, p_field_name tstr100, p_field_caption tstr100, p_field_tooltip tstr100)
 RETURNS void
 LANGUAGE plpgsql
AS $function$

begin 

INSERT INTO core_appfieldscaptions (user_id, app_id, field_name, field_caption, field_tooltip) 
	VALUES (p_user_id, p_app_id, p_field_name, p_field_caption, p_field_tooltip)
                     ON CONFLICT (user_id, app_id, field_name) DO UPDATE SET 
                       field_caption = EXCLUDED.field_caption,
                       field_tooltip = EXCLUDED.field_tooltip; 
end;

$function$
