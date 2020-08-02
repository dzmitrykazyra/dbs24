INSERT INTO counterparties (counterparty_id, counterparty_code, short_name, full_name) 
	VALUES (1, 'N1', 'йкхемр1', 'йкхемр мнлеп ндхм'),
		(2, 'N2', 'йкхемр2', 'йкхемр мнлеп дбю')
 ON CONFLICT (counterparty_id) DO UPDATE 
	SET counterparty_code= EXCLUDED.counterparty_code,
	short_name= EXCLUDED.short_name,
	full_name= EXCLUDED.full_name;