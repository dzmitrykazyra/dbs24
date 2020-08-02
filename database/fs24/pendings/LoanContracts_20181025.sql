INSERT INTO loancontracts (contract_id, loan_source_id) 
	VALUES (13, 100),  (14, 100)
 ON CONFLICT (contract_id) DO UPDATE SET loan_source_id = EXCLUDED.loan_source_id;

