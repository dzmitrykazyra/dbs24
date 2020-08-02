DROP FUNCTION pmt_sched_save;
CREATE OR REPLACE FUNCTION core_pmt_sched_save(p_schedule_id TIdBigCode, p_contract_id TIdBigCode, p_entity_kind_id tidcode, p_pmt_term_id tidcode, p_schedule_alg_id tidcode, p_from_date tdate, p_last_date tdate)
 RETURNS void
 LANGUAGE plpgsql
AS $function$

begin 


INSERT INTO core_pmtschedules (schedule_id, contract_id, entity_kind_id, pmt_term_id, schedule_alg_id, from_date, last_date) 
	VALUES (p_schedule_id, p_contract_id, p_entity_kind_id, p_pmt_term_id, p_schedule_alg_id, p_from_date, p_last_date)
               ON CONFLICT (schedule_id) DO UPDATE SET 
                       contract_id = EXCLUDED.contract_id,
                       entity_kind_id = EXCLUDED.entity_kind_id,
                       pmt_term_id = EXCLUDED.pmt_term_id,
                        schedule_alg_id = EXCLUDED.schedule_alg_id,
                        from_date = EXCLUDED.from_date,
                        last_date = EXCLUDED.last_date; 
end;

$function$
