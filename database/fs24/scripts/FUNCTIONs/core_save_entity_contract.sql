DROP FUNCTION core_save_entity_contract;
CREATE OR REPLACE FUNCTION core_save_entity_contract(p_entity_id tidbigcode, p_contract_subject_id tidcode, p_counterparty_id tidbigcode, p_entity_kind_id tidcode, p_currency_id tidcode, p_contract_num tstr200, p_contract_date tdate, p_begin_date tdate, p_end_date tdate, p_contract_summ tmoney, p_tariff_plan_id tidbigcode)
 RETURNS void
 LANGUAGE plpgsql
AS $function$

begin 

INSERT INTO core_entityContracts (
                     contract_id,
                     contract_subject_id,
                     counterparty_id,
                     entity_kind_id,
                     currency_id,
                     contract_num,
                     contract_date,
                     begin_date,
                     end_date,
                     contract_summ,
		     tariff_plan_id) 
                    	VALUES (p_entity_id, p_contract_subject_id, p_counterparty_id, p_entity_kind_id, p_currency_id, p_contract_num, p_contract_date, p_begin_date, p_end_date, p_contract_summ, p_tariff_plan_id)
                     ON CONFLICT (contract_id) DO UPDATE SET 
                       contract_subject_id = EXCLUDED.contract_subject_id,
                       counterparty_id = EXCLUDED.counterparty_id,
                       entity_kind_id = EXCLUDED.entity_kind_id,
                       currency_id = EXCLUDED.currency_id,
                       contract_num = EXCLUDED.contract_num,
                       contract_date = EXCLUDED.contract_date,
                       begin_date = EXCLUDED.begin_date,
                       end_date = EXCLUDED.end_date,
                       tariff_plan_id = EXCLUDED.tariff_plan_id,
                       contract_summ = EXCLUDED.contract_summ; 


end;

$function$ 