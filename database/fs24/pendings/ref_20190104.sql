INSERT INTO actioncodesref (action_code, action_name, app_name) 
	VALUES (150, '�������� �150', 'security-manager');
INSERT INTO actioncodesref (action_code, action_name, app_name) 
	VALUES (101500001, 'Action 101500001', '44.ft');
INSERT INTO actioncodesref (action_code, action_name, app_name) 
	VALUES (101400001, '�������� � 101400001', 'loc.dt');
INSERT INTO actioncodesref (action_code, action_name, app_name) 
	VALUES (100001, '�������� ��� �������������� ������������', 'security-manager');
INSERT INTO actioncodesref (action_code, action_name, app_name) 
	VALUES (100002, '�������� ��� �������������� ����', 'security-manager');
INSERT INTO actioncodesref (action_code, action_name, app_name) 
	VALUES (101100001, '������ �������', 'LoanContracts.dll');
INSERT INTO actioncodesref (action_code, action_name, app_name) 
	VALUES (101200001, '����������', 'LoanContracts.dll');
INSERT INTO actioncodesref (action_code, action_name, app_name) 
	VALUES (101300001, '�������������� �����������', 'LoanContracts.dll');


INSERT INTO applicationsref (app_id, app_code, app_name, app_url) 
	VALUES (101, 'APP.113', '������ ������', 'securities');
INSERT INTO applicationsref (app_id, app_code, app_name, app_url) 
	VALUES (104, 'app.113.4', '������� ������������ �����', 'loan-contracts/bla-bla-bla.xhtml');
INSERT INTO applicationsref (app_id, app_code, app_name, app_url) 
	VALUES (100, 'APP.13', '������� ���������� �����', 'loan-contracts/��-��');
INSERT INTO applicationsref (app_id, app_code, app_name, app_url) 
	VALUES (1001, 'USERS', '�������� ������������', 'security-manager/user.xhtml');
INSERT INTO applicationsref (app_id, app_code, app_name, app_url) 
	VALUES (1100, 'RUNAPP', '����� ������� ����������', 'security-manager/run-application-form.xhtml');
INSERT INTO applicationsref (app_id, app_code, app_name, app_url) 
	VALUES (1002, 'ROLES', '���������������� ����', 'security-manager/role.xhtml');
INSERT INTO applicationsref (app_id, app_code, app_name, app_url) 
	VALUES (102, 'lc.fl', '������� �������������� �����', 'loan-contracts/Loan2Individual.xhtml');
INSERT INTO applicationsref (app_id, app_code, app_name, app_url) 
	VALUES (105, 'LOAN.MASSOPER', '�������� ��������', 'loan-contracts/massopers-form.xhtml');


INSERT INTO contractsubjectsref (contract_subject_id, contract_subject_name) 
	VALUES (210001, '���������� ������� � ����� ������� ���������� �����');
INSERT INTO contractsubjectsref (contract_subject_id, contract_subject_name) 
	VALUES (210002, '���������� ������� � ����� ����� ���������� �����');

INSERT INTO entitykindsref (entity_kind_id, entity_type_id, entity_kind_name) 
	VALUES (21000010, 2100, '������ �� � ������� �� ��������');
INSERT INTO entitykindsref (entity_kind_id, entity_type_id, entity_kind_name) 
	VALUES (21000020, 2100, '������ �� � ����� ����������');
INSERT INTO entitykindsref (entity_kind_id, entity_type_id, entity_kind_name) 
	VALUES (21000030, 2100, '������ ���������� �����');

INSERT INTO entitystatusesref (entity_status_id, entity_type_id, entity_status_name) 
	VALUES (1, 100, '����������� ������������');
INSERT INTO entitystatusesref (entity_status_id, entity_type_id, entity_status_name) 
	VALUES (-1, 100, '������������� ������������');
INSERT INTO entitystatusesref (entity_status_id, entity_type_id, entity_status_name) 
	VALUES (1, 101, '����������� ����');
INSERT INTO entitystatusesref (entity_status_id, entity_type_id, entity_status_name) 
	VALUES (-1, 101, '�������������� ����');
INSERT INTO entitystatusesref (entity_status_id, entity_type_id, entity_status_name) 
	VALUES (0, 2100, '����������� �������');
INSERT INTO entitystatusesref (entity_status_id, entity_type_id, entity_status_name) 
	VALUES (1, 2100, '�������� �������');
INSERT INTO entitystatusesref (entity_status_id, entity_type_id, entity_status_name) 
	VALUES (1, 2200, '�������� �������');
INSERT INTO entitystatusesref (entity_status_id, entity_type_id, entity_status_name) 
	VALUES (0, 2200, '����������� �������');


INSERT INTO entitytypesref (entity_type_id, entity_type_name) 
	VALUES (100, '������������ �������');
INSERT INTO entitytypesref (entity_type_id, entity_type_name) 
	VALUES (101, '���������������� ����');
INSERT INTO entitytypesref (entity_type_id, entity_type_name) 
	VALUES (2100, '������ ����������� ����');
INSERT INTO entitytypesref (entity_type_id, entity_type_name) 
	VALUES (2200, '������ ������������ ����');
INSERT INTO entitytypesref (entity_type_id, entity_type_name) 
	VALUES (2300, '������������� ������');
INSERT INTO entitytypesref (entity_type_id, entity_type_name) 
	VALUES (2400, '������ �� ������������ ������ �����');

INSERT INTO liasactiontypesref (action_type_id, change_rest_tag, action_type_name) 
	VALUES (1, true, '�������� � ����� �������������');
INSERT INTO liasactiontypesref (action_type_id, change_rest_tag, action_type_name) 
	VALUES (2, true, '���� �������� �� �������� ��������������');
INSERT INTO liasactiontypesref (action_type_id, change_rest_tag, action_type_name) 
	VALUES (3, true, '������ � ����� �������������');
INSERT INTO liasactiontypesref (action_type_id, change_rest_tag, action_type_name) 
	VALUES (4, true, '���������� ����� �������������');
INSERT INTO liasactiontypesref (action_type_id, change_rest_tag, action_type_name) 
	VALUES (5, false, '���������� ������������');
INSERT INTO liasactiontypesref (action_type_id, change_rest_tag, action_type_name) 
	VALUES (6, false, '����������� ����� �� ��������������');
INSERT INTO liasactiontypesref (action_type_id, change_rest_tag, action_type_name) 
	VALUES (7, true, '���������� ����� �������������');
INSERT INTO liasactiontypesref (action_type_id, change_rest_tag, action_type_name) 
	VALUES (8, false, '�������������� ��������� �������������');
INSERT INTO liasactiontypesref (action_type_id, change_rest_tag, action_type_name) 
	VALUES (9, true, '������������� ������������');

INSERT INTO liasbaseassettypesref (base_asset_type_id, base_asset_type_name) 
	VALUES (1, '�������� ��������');
INSERT INTO liasbaseassettypesref (base_asset_type_id, base_asset_type_name) 
	VALUES (2, '�������� ��������');
INSERT INTO liasbaseassettypesref (base_asset_type_id, base_asset_type_name) 
	VALUES (3, '������ ������');
INSERT INTO liasbaseassettypesref (base_asset_type_id, base_asset_type_name) 
	VALUES (4, '����������� �������');
INSERT INTO liasbaseassettypesref (base_asset_type_id, base_asset_type_name) 
	VALUES (5, '��������� �������');
INSERT INTO liasbaseassettypesref (base_asset_type_id, base_asset_type_name) 
	VALUES (6, '������������ (����� �����)');
INSERT INTO liasbaseassettypesref (base_asset_type_id, base_asset_type_name) 
	VALUES (7, '����� ���������');
INSERT INTO liasbaseassettypesref (base_asset_type_id, base_asset_type_name) 
	VALUES (8, '���������');
INSERT INTO liasbaseassettypesref (base_asset_type_id, base_asset_type_name) 
	VALUES (9, '�������� �������');
INSERT INTO liasbaseassettypesref (base_asset_type_id, base_asset_type_name) 
	VALUES (10, '���������� ������');
INSERT INTO liasbaseassettypesref (base_asset_type_id, base_asset_type_name) 
	VALUES (11, '������������� ������������� (�����)');
INSERT INTO liasbaseassettypesref (base_asset_type_id, base_asset_type_name) 
	VALUES (12, '������������� �����');
INSERT INTO liasbaseassettypesref (base_asset_type_id, base_asset_type_name) 
	VALUES (13, '����������� �������������');
INSERT INTO liasbaseassettypesref (base_asset_type_id, base_asset_type_name) 
	VALUES (14, '������');

INSERT INTO liasdebtstatesref (debt_state_id, debt_state_name) 
	VALUES (1, '���������� ������������� ');
INSERT INTO liasdebtstatesref (debt_state_id, debt_state_name) 
	VALUES (2, '�������� ���������������� ������������� ');
INSERT INTO liasdebtstatesref (debt_state_id, debt_state_name) 
	VALUES (3, '������������ ���������������� ������������� ');
INSERT INTO liasdebtstatesref (debt_state_id, debt_state_name) 
	VALUES (4, '������������ ������������� ');
INSERT INTO liasdebtstatesref (debt_state_id, debt_state_name) 
	VALUES (5, '�� �������������� � ���� ������������� ');
INSERT INTO liasdebtstatesref (debt_state_id, debt_state_name) 
	VALUES (6, '�����������  ������������� ');
INSERT INTO liasdebtstatesref (debt_state_id, debt_state_name) 
	VALUES (7, '������������� �� ��������������');
INSERT INTO liasdebtstatesref (debt_state_id, debt_state_name) 
	VALUES (61, '���������� ������� �������������');
INSERT INTO liasdebtstatesref (debt_state_id, debt_state_name) 
	VALUES (62, '���������� ���������������� �������������');
INSERT INTO liasdebtstatesref (debt_state_id, debt_state_name) 
	VALUES (63, '���������� ������������ ������������� ');

INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (0, '�������� �� ���������� � ��������� ����� ������� ������������ ');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (1, '���� �������� �� �������������� ');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (2, '���������� ������� ');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (3, '����������� �������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (4, '���������� ���������� � ������������ �������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (5, '���������� ���������� � ������������ �������� �����');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (6, '�������� ������� ������������ ��������� (��������)');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (7, '�������� ������� ������������ ��������� (��������)');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (9, '������� ����������� �������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (10, '������� ������������ �������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (11, '����������� ���������� � ������ �������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (12, '������� ����������� � ������������� ��������������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (13, '����� �� ��������� ������������� �� ����������� ���������)');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (14, '����������� ������������� �� ����������� ���������)');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (15, '�������������� ����� (���������� �������))');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (16, '������������� ���������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (17, '�������������� ����������� ���������� � ������ �������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (18, '������ �� �������� ���������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (20, '���������������� ������� � �������� �� �������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (21, '���������������� �������������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (22, '����������� ������������� �� �������� "��������"');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (23, '������������� ���������� �������� �� �������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (24, '�������������� ������������� �� ������������ ���������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (25, '���������� ������� � ���������� ������������ �������������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (26, '���������� ����� �������������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (27, '���������� ����� �������������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (28, '�������������� ������������� ��� �������� �� ������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (29, '������� ������������ ������������� �� ������ ������� ��������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (30, '���������� ������������ � ���� ��������� ����������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (31, '��������� ������� ��������� �������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (32, '�������������� ����� ��������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (33, '��������� ������������� �� ���� ���������� �������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (34, '�������� � ������ ������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (35, '��������� �� ��������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (36, '������� ����������� ������ �����');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (37, '����� ����������� ������ �����');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (38, '���������� ��������� ��� ����������� ������������� ���������� ��');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (39, '���������� ��������� ��� ����������� ������������� ���������� ��');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (40, '���������� ��������� ��� ����������� ������������� ���������� ��');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (41, '��������� ������������ �������������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (42, '��������� ������������ �������������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (43, '��������� ����������� �������������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (44, '��������� ����������� �������������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (45, '���������� ������� �� �������� ����');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (46, '��������� ������������� �� ������� ������� ��������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (47, '��������� �������� ��������� �������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (48, '����������� ������� �� �������� ����');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (49, '����������� ����� ��������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (50, '����� �� ��������� ������������� �� ������ �������������� �����');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (51, '������� ����������� ������������� �� ������ ����');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (52, '������ ������� � ������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (53, '��������� ������� � ������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (54, '���������� ������� ��� ������������� � ��������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (55, '��������� ������������� � ���� ������������� � ��������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (56, '������� ����� �� ������ ���� �� �������� ���������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (57, '������� ����� �� ������ ���� �� ��������� ���������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (58, '����������� ������� �� �������������� � ��������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (59, '������������ ������� ���������� � ����������� ���������� �������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (60, '��������� ������������ �����');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (61, '��������� ������������� �� ���� ������� � ��������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (62, '��������������� ���������� ����������� ������� � ��������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (63, '��������� ������������� �� ���� ����������� ������ � ��������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (64, '���������� ���� �� �������������� ��������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (65, '�������� � ����� ����������� ������� �� ��������� ������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (66, '�������� �������������� ������� �� ����������� �������������� ��');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (67, '���������� ������� � �������������� ������������� ������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (68, '������� ������������ ������� � �������������� ������������� ����');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (69, '������������� ������������� �� ��������� � ����������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (70, '���������� �������� � ����� � ���������� �������� �������������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (71, '���������� �������� � ����� � ���������� ������������ �����');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (80, '���������� �������� � ����� � ����������� ������� ������������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (81, '���������� �������� � ����� � ���������� �������� �������������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (82, '���������� �������� � ����� � ���������� ������������ �����');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (83, '���������� �������� �� ��������� ����� �������� �������������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (85, '���������� ��������� ��� ����������� ������������� ���������� ��');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (86, '�������� ������������ ��������� ������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (87, '����� ����������� ������ ����� � ���� ������������ � ��������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (88, '����� �� ��������� ������������� �� ��������� �������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (91, '���������� �������� �� �������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (93, '���������� ������� �� �������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (101, '�������� � ����� ��������� �������������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (102, '���������� ����� ��������� �������������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (103, '���������� ����� ��������� �������������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (104, '���������� ����� � ����� � ���������� �������������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (105, '������ � ����� �������������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (106, '�������� ���������� ������������� �� ������������ �������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (107, '�������� ������������ ����������� ��������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (108, '������ � ����� ����������� �������������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (110, '�������� � ����� ���������� � ������� ������� �������������');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (111, '����� �� ��������� ���������� �������������');

INSERT INTO liasgroupsref (lias_group_id, lias_group_name) 
	VALUES (1, '������������� ��������� �����');
INSERT INTO liasgroupsref (lias_group_id, lias_group_name) 
	VALUES (2, '������������� �� ������������ ��������');
INSERT INTO liasgroupsref (lias_group_id, lias_group_name) 
	VALUES (3, '������������� �� ����������� ������� �������� � ��������');
INSERT INTO liasgroupsref (lias_group_id, lias_group_name) 
	VALUES (4, '������������� ��������� �����');
INSERT INTO liasgroupsref (lias_group_id, lias_group_name) 
	VALUES (5, '������������� �� ����������� ');
INSERT INTO liasgroupsref (lias_group_id, lias_group_name) 
	VALUES (6, '������������� �� ���������');
INSERT INTO liasgroupsref (lias_group_id, lias_group_name) 
	VALUES (7, '������������� �� ��������');
INSERT INTO liasgroupsref (lias_group_id, lias_group_name) 
	VALUES (8, '������������� ��������� �����');
INSERT INTO liasgroupsref (lias_group_id, lias_group_name) 
	VALUES (9, '������� �������');
INSERT INTO liasgroupsref (lias_group_id, lias_group_name) 
	VALUES (10, '������������� �� �������������� ���������');
INSERT INTO liasgroupsref (lias_group_id, lias_group_name) 
	VALUES (11, '������������� �� ������������� ������������ �����');

INSERT INTO liaskindsref (lias_kind_id, is_claim, lias_group_id, lias_kind_name) 
	VALUES (1, true, 1, '���������� �� �������� ��������� ������ ');
INSERT INTO liaskindsref (lias_kind_id, is_claim, lias_group_id, lias_kind_name) 
	VALUES (2, false, 1, '������������� �� �������� ��������� ������');
INSERT INTO liaskindsref (lias_kind_id, is_claim, lias_group_id, lias_kind_name) 
	VALUES (3, true, 6, '���������� �� ������ ���������');
INSERT INTO liaskindsref (lias_kind_id, is_claim, lias_group_id, lias_kind_name) 
	VALUES (4, false, 6, '������������� �� ������ ���������');
INSERT INTO liaskindsref (lias_kind_id, is_claim, lias_group_id, lias_kind_name) 
	VALUES (5, false, 7, '����������� ������� �� �������� ��������� ������� �� �������, ������������ ���������� �����');
INSERT INTO liaskindsref (lias_kind_id, is_claim, lias_group_id, lias_kind_name) 
	VALUES (6, false, 1, '������������� �� �������� ���������������� ��������');
INSERT INTO liaskindsref (lias_kind_id, is_claim, lias_group_id, lias_kind_name) 
	VALUES (7, true, 1, '������������� ����� �� �������� ����������������� ��������');
INSERT INTO liaskindsref (lias_kind_id, is_claim, lias_group_id, lias_kind_name) 
	VALUES (8, true, 1, '����������� ������ ������������� �� �����������');
INSERT INTO liaskindsref (lias_kind_id, is_claim, lias_group_id, lias_kind_name) 
	VALUES (9, true, 2, '���������� �� ������������ ��������');
INSERT INTO liaskindsref (lias_kind_id, is_claim, lias_group_id, lias_kind_name) 
	VALUES (10, true, 2, '��������� ���������, ������������� ��� �������');

INSERT INTO liastypesref (lias_type_id, lias_type_name) 
	VALUES (1, '��������� ������������� (������������� �� �������������� ��������� ������) ');
INSERT INTO liastypesref (lias_type_id, lias_type_name) 
	VALUES (2, '������� ������������� (������������� �� �������� ��������� ������) ');
INSERT INTO liastypesref (lias_type_id, lias_type_name) 
	VALUES (3, '����������� ������������� (������������� �� ����������) ');
INSERT INTO liastypesref (lias_type_id, lias_type_name) 
	VALUES (4, '������������� �� ��������� (��������� �������������)  ');
INSERT INTO liastypesref (lias_type_id, lias_type_name) 
	VALUES (5, '������������� �� �������� (��������� �������������)   ');

INSERT INTO loansourcesref (loan_source_id, loan_source_name) 
	VALUES (100, '�������� �����');
INSERT INTO loansourcesref (loan_source_id, loan_source_name) 
	VALUES (101, '����������� �������� �����');
INSERT INTO loansourcesref (loan_source_id, loan_source_name) 
	VALUES (102, '������������ ������� �����');
INSERT INTO loansourcesref (loan_source_id, loan_source_name) 
	VALUES (200, '�������� ��������');
INSERT INTO loansourcesref (loan_source_id, loan_source_name) 
	VALUES (201, '�������� �������� �������');
INSERT INTO loansourcesref (loan_source_id, loan_source_name) 
	VALUES (202, '�������� ���������������� �������');
INSERT INTO loansourcesref (loan_source_id, loan_source_name) 
	VALUES (300, '���������������� ��������� �������');
INSERT INTO loansourcesref (loan_source_id, loan_source_name) 
	VALUES (400, '�������� ������������ �����');
INSERT INTO loansourcesref (loan_source_id, loan_source_name) 
	VALUES (500, '�������� ����.������');
INSERT INTO loansourcesref (loan_source_id, loan_source_name) 
	VALUES (600, '�������� ������������� ������ � �����������');


INSERT INTO registryparams (param_id, parent_param_id, param_name, param_value, param_descr) 
	VALUES (1, NULL, 'logSqlQuery', '1', '��� sql ��������');
