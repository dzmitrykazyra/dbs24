DROP FUNCTION ent_insertorupdate_actioncodesref;
CREATE OR REPLACE FUNCTION core_insertorupdate_actioncodesref(p_action_code tidcode, p_action_name tstr80, p_app_name tstr80, p_is_closed tboolean)
 RETURNS void
 LANGUAGE plpgsql
AS $function$
begin 
    
    INSERT INTO core_ActionCodesRef(action_code, action_name, app_name, is_closed)
      VALUES (p_action_code, p_action_name, p_app_name, p_is_closed)
      ON CONFLICT (action_code) DO
    UPDATE
      SET app_name = EXCLUDED.app_name,
     action_name = EXCLUDED.action_name,
    is_closed = EXCLUDED.is_closed;

end

$function$ 