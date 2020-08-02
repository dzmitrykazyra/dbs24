DROP FUNCTION loan_get_loan_contract;
CREATE OR REPLACE FUNCTION rlc_get_loan_contract(p_entity_id TIdBigCode)
 RETURNS refcursor
 LANGUAGE plpgsql
AS $function$ declare default_cursor refcursor;

begin open default_cursor for 
SELECT lc.* 
FROM  rlc_LoanContracts lc 
WHERE lc.contract_id= p_entity_id;

return(default_cursor);
end;

$function$ 