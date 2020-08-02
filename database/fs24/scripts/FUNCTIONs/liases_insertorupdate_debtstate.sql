CREATE OR REPLACE FUNCTION liases_insertorupdate_debtstate(
	p_debt_state_id tidcode,
	p_debt_state_name tstr100)
    RETURNS void
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$

begin 
    
INSERT INTO liasdebtstatesref (debt_state_id, debt_state_name) 
	VALUES (p_debt_state_id, p_debt_state_name)
      ON CONFLICT (debt_state_id) DO
    UPDATE
        SET debt_state_name = EXCLUDED.debt_state_name;

end

$BODY$;