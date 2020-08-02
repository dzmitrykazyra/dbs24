DROP FUNCTION tariff_get_accretion_history;
CREATE OR REPLACE FUNCTION tariff_get_accretion_history(p_contract_id TIdBigCode)
 RETURNS refcursor
 LANGUAGE plpgsql
AS $function$

 declare default_cursor refcursor;

begin open default_cursor for select
    ta.*	
from
    tariffaccretionshist ta, liasactions la
where 
    ta.contract_id = p_contract_id
    and ta.lias_action_id = la.lias_action_id
    and la.fin_oper_status_id=1;

return(default_cursor);
end;

$function$