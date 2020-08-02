CREATE OR REPLACE FUNCTION liases_insertorupdate_liaskind(
	p_lias_kind_id tidcode,
	p_is_claim tboolean,
	p_lias_group_id tidcode,
	p_lias_kind_name tstr100)
    RETURNS void
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$

begin 
    
INSERT INTO liaskindsref (lias_kind_id, is_claim, lias_group_id, lias_kind_name) 
	VALUES (p_lias_kind_id, p_is_claim, p_lias_group_id, p_lias_kind_name)
      ON CONFLICT (lias_kind_id) DO
    UPDATE
      SET is_claim = EXCLUDED.is_claim,
        lias_group_id = EXCLUDED.lias_group_id,
        lias_kind_name = EXCLUDED.lias_kind_name;

end

$BODY$;