-- FUNCTION: ee_dev.core_insertorupdate_applicationsref(tiduser, tstr30, tstr50, tstr100)

-- DROP FUNCTION ee_dev.core_insertorupdate_applicationsref(tiduser, tstr30, tstr50, tstr100);

CREATE OR REPLACE FUNCTION core_insertorupdate_applicationsref(
	p_app_id tiduser,
	p_app_code tstr30,
	p_app_name tstr50,
	p_app_url tstr100)
    RETURNS void
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$

begin 
    
INSERT INTO core_applicationsref (app_id, app_code, app_name, app_url) 
	VALUES (p_app_id, p_app_code, p_app_name, p_app_url)
      ON CONFLICT (app_id) DO
    UPDATE
      SET app_code = EXCLUDED.app_code,
        app_name = EXCLUDED.app_name,
        app_url = EXCLUDED.app_url;

end

$BODY$;
