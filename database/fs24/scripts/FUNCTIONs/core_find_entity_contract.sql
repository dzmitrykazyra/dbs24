--DROP FUNCTION core_find_entity_contract;
CREATE OR REPLACE FUNCTION core_find_entity_contract(p_entity_id TIdBigCode, p_entity_type_id tidcode, p_contract_num tstr200)
 RETURNS refcursor
 LANGUAGE plpgsql
AS $function$ declare default_cursor refcursor;

begin open default_cursor for 
SELECT e.entity_id
                FROM core_entities e
                LEFT JOIN core_entityContracts ec ON (e.entity_id=ec.contract_id)
                WHERE (e.entity_id=p_entity_id
                       OR LOWER(ec.contract_num) = LOWER(p_contract_num))
                  AND e.entity_type_id = p_entity_type_id;

return(default_cursor);
end;

$function$
