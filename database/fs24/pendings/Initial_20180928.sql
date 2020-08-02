INSERT INTO entityTypeRef (entity_type_id, entity_title) 
	VALUES  (100, 'Пользователь системы'), 
		(101, 'Пользовательская роль')
    ON CONFLICT (entity_type_id) DO UPDATE SET entity_title = EXCLUDED.entity_title;

INSERT INTO entityStatusesRef (entity_status_id, entity_type_id, entity_status_name) 
	VALUES (1, 100, 'Действующий пользователь'), 
		(-1, 100, 'Блокированный пользователь'), 
		(1, 101, 'Действующая роль'), 
		(-1, 101, 'Аннулированная роль')
    ON CONFLICT (entity_status_id, entity_type_id) DO UPDATE SET entity_status_name = EXCLUDED.entity_status_name;

INSERT INTO entities (entity_id, entity_type_id, entity_status_id, creation_date, close_date, last_modify) 
	VALUES (1, 100, 1, '2018-09-19', NULL, '2018-09-19 19:22:57.775438')
    ON CONFLICT (entity_id) DO UPDATE SET entity_status_id = EXCLUDED.entity_status_id, entity_type_id = EXCLUDED.entity_type_id;

INSERT INTO users (user_id, login, password, "name", phone, mail) 
	VALUES (1, 'kdg', 'vbju6NqLo3mTxkChDeUiyOfLqAbGyfcIYZjGPQNOwXE=', 'kdg', NULL, NULL)
    ON CONFLICT (user_id) DO UPDATE SET login = EXCLUDED.login, "name" = EXCLUDED."name", password = EXCLUDED.password;

INSERT INTO applicationsref (app_id, app_code, app_name, app_url) 
	VALUES (100, 'APP.13', 'Приложение №1313', 'www.tut.by\by')
    ON CONFLICT (app_id, app_code) DO UPDATE SET app_name = EXCLUDED.app_name, "app_url" = EXCLUDED."app_url";

commit;