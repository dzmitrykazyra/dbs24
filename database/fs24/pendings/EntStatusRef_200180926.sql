INSERT INTO entstatusesref (entstatus_id, entref_id, entstatus_name) 
	VALUES (1, 100, '����������� ������������'), 
		(-1, 100, '������������� ������������'), 
		(1, 101, '����������� ����'), 
		(-1, 101, '�������������� ����')
    ON CONFLICT (entstatus_id, entref_id) DO UPDATE SET entstatus_name = EXCLUDED.entstatus_name;