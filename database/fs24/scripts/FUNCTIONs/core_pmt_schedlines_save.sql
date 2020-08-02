DROP FUNCTION pmt_schedlines_save;
CREATE OR REPLACE FUNCTION core_pmt_schedlines_save(p_schedule_id TIdBigCode, p_actual_date tdate, p_from_date tdate, p_to_date tdate, p_appear_date tdate, p_pay_sum tmoney, p_calc_date tdate)
 RETURNS void
 LANGUAGE plpgsql
AS $function$

begin 

INSERT INTO core_pmtschedulelines (schedule_id, actual_date, from_date, to_date, appear_date, pay_sum, calc_date) 
	VALUES (p_schedule_id, p_actual_date, p_from_date, p_to_date, p_appear_date, p_pay_sum, p_calc_date)
               ON CONFLICT (schedule_id, actual_date, from_date, to_date) DO UPDATE SET 
                       appear_date = EXCLUDED.appear_date,
                       pay_sum = EXCLUDED.pay_sum,
                        calc_date = EXCLUDED.calc_date; 
end;

$function$
