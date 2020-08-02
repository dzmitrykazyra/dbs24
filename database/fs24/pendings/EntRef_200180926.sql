INSERT INTO entref (entref_id, enttitle) 
	VALUES  (100, 'Пользователь системы'), 
		(101, 'Пользовательская роль')
    ON CONFLICT (entref_id) DO UPDATE SET enttitle = EXCLUDED.enttitle;