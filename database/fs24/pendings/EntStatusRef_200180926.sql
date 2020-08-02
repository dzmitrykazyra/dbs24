INSERT INTO entstatusesref (entstatus_id, entref_id, entstatus_name) 
	VALUES (1, 100, 'Действующий пользователь'), 
		(-1, 100, 'Блокированный пользователь'), 
		(1, 101, 'Действующая роль'), 
		(-1, 101, 'Аннулированная роль')
    ON CONFLICT (entstatus_id, entref_id) DO UPDATE SET entstatus_name = EXCLUDED.entstatus_name;