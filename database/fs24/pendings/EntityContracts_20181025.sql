INSERT INTO entitycontracts (contract_id, contract_subject_id, counterparty_id, entity_kind_id, currency_code, contract_num, contract_date, begin_date, end_date, contract_currency_code, contract_summ) 
	VALUES (13, 210002, 1, 21000010, 840, '3E', '2018-10-18', '2018-10-18', '2018-10-18', 840, 15000.0000),
	 (14, 210002, 1, 21000010, 978, '33', '2018-10-19', '2018-10-19', '2018-10-19', 978, 98000.0000)
 ON CONFLICT (contract_id) DO UPDATE SET contract_summ = EXCLUDED.contract_summ, currency_code = EXCLUDED.currency_code, contract_currency_code = EXCLUDED.contract_currency_code;


