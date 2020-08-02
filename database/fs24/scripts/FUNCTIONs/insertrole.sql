DROP FUNCTION insertrole;
CREATE OR REPLACE FUNCTION insertrole(
	p_role_id TIdBigCode,
	p_role_code tstr30,
	p_role_name tstr50)
    RETURNS void
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$

begin 

  INSERT INTO roles(role_id, role_code, role_name)
    VALUES(p_role_id, p_role_code, p_role_name) ON CONFLICT (role_id) DO
    UPDATE
      SET role_code = EXCLUDED.role_code,
          role_name = EXCLUDED.role_name;
end;

$BODY$;
