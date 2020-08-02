INSERT INTO entities (entity_id, entity_type_id, entity_status_id, creation_date, close_date, last_modify) 
	VALUES (1, 100, 1, '2018-09-19', NULL, '2018-09-19 19:22:57.775438'),
		(2, 100, 1, '2018-10-18', NULL, '2018-10-18 11:44:00.191'),
		(13, 2100, 1, '2018-10-18', NULL, '2018-10-18 11:44:00.191'),
		(14, 2100, 1, '2018-10-19', '2018-10-19', '2018-10-19 14:27:09.564')
    ON CONFLICT (entity_id) DO UPDATE SET entity_status_id = EXCLUDED.entity_status_id, entity_type_id = EXCLUDED.entity_type_id;