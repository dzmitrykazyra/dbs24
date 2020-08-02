DROP FUNCTION save_loan_contract;
CREATE OR REPLACE FUNCTION rlc_save_loan_contract(p_contract_id TIdBigCode, p_loan_source_id tidcode, p_pmt_term_id tidcode, p_schedule_alg_id tidcode)
 RETURNS void
 LANGUAGE plpgsql
AS $function$

begin 

INSERT INTO rlc_loancontracts (contract_id, 
                     loan_source_id,
                     pmt_term_id,
                     schedule_alg_id) 
                    	VALUES (p_contract_id, p_loan_source_id, p_pmt_term_id, p_schedule_alg_id)
                     ON CONFLICT (contract_id) DO UPDATE SET 
                       loan_source_id = EXCLUDED.loan_source_id,
                       pmt_term_id = EXCLUDED.pmt_term_id,
                       schedule_alg_id = EXCLUDED.schedule_alg_id; 
end;

$function$
