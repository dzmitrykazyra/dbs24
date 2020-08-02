CREATE OR REPLACE FUNCTION liases_insertorupdate_liasfinopercode(
	p_fin_oper_code tidcode,
	p_fin_oper_name tstr100)
    RETURNS void
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$

begin 
    
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (p_fin_oper_code, p_fin_oper_name)
      ON CONFLICT (fin_oper_code) DO
    UPDATE
      SET fin_oper_name = EXCLUDED.fin_oper_name;

end

$BODY$;