INSERT INTO entref (entref_id, enttitle) 
	VALUES  (100, '������������ �������'), 
		(101, '���������������� ����')
    ON CONFLICT (entref_id) DO UPDATE SET enttitle = EXCLUDED.enttitle;