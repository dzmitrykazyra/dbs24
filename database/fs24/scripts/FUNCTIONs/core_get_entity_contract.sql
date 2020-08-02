DROP FUNCTION core_get_entity_contract;
CREATE OR REPLACE FUNCTION core_get_entity_contract(p_entity_id TIdBigCode)
 RETURNS refcursor
 LANGUAGE plpgsql
AS $function$ declare default_cursor refcursor;

begin open default_cursor for select
	ec.*
from
	core_EntityContracts ec
where
	ec.contract_id = p_entity_id;

return(default_cursor);
end;

$function$
