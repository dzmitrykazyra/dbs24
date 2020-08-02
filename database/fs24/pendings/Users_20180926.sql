INSERT INTO users (user_id, login, password, "name", phone, mail) 
	VALUES (1, 'kdg', 'vbju6NqLo3mTxkChDeUiyOfLqAbGyfcIYZjGPQNOwXE=', 'kdg', NULL, NULL)
    ON CONFLICT (user_id) DO UPDATE SET login = EXCLUDED.login, "name" = EXCLUDED."name", password = EXCLUDED.password;


