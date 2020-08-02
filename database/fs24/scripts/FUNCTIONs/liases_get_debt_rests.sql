CREATE OR REPLACE FUNCTION liases_get_debt_rests(
	p_debt_id tidcode)
    RETURNS refcursor
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$
 declare default_cursor refcursor;

begin open default_cursor for 
SELECT ldr.* 
  FROM  LiasDebtRests ldr 
  WHERE ldr.debt_id= p_debt_id 
    and ldr.rest_type=1; 

return(default_cursor);
end;

$BODY$;