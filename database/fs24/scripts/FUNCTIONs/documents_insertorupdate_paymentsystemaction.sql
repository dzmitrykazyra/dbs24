DROP FUNCTION documents_insertorupdate_paymentsystemaction;

CREATE OR REPLACE FUNCTION documents_insertorupdate_PaymentSystemDocTemplate(
	p_pmt_sys_id tidcode,
	p_fin_oper_code tidcode,
	p_doc_template_id tidcode,
	p_actual_date tdate,
	p_close_date tdate)
    RETURNS void
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$
 begin 
    INSERT INTO paymentsystemdoctemplateref (pmt_sys_id, fin_oper_code, doc_template_id, actual_date, close_date) 
	VALUES (p_pmt_sys_id, p_fin_oper_code, p_doc_template_id, p_actual_date, p_close_date) on
		conflict(pmt_sys_id, fin_oper_code, doc_template_id)
         do update
		set
			actual_date = EXCLUDED.actual_date,
                        close_date = EXCLUDED.close_date;
end 
$BODY$;