INSERT INTO actioncodesref (action_code, action_name, app_name) 
	VALUES (150, 'Действие №150', 'security-manager');
INSERT INTO actioncodesref (action_code, action_name, app_name) 
	VALUES (101500001, 'Action 101500001', '44.ft');
INSERT INTO actioncodesref (action_code, action_name, app_name) 
	VALUES (101400001, 'Действие № 101400001', 'loc.dt');
INSERT INTO actioncodesref (action_code, action_name, app_name) 
	VALUES (100001, 'Создание или редактирование пользователя', 'security-manager');
INSERT INTO actioncodesref (action_code, action_name, app_name) 
	VALUES (100002, 'Создание или редактирование роли', 'security-manager');
INSERT INTO actioncodesref (action_code, action_name, app_name) 
	VALUES (101100001, 'Выдача кредита', 'LoanContracts.dll');
INSERT INTO actioncodesref (action_code, action_name, app_name) 
	VALUES (101200001, 'Начисление', 'LoanContracts.dll');
INSERT INTO actioncodesref (action_code, action_name, app_name) 
	VALUES (101300001, 'Урегулирование начисленных', 'LoanContracts.dll');


INSERT INTO applicationsref (app_id, app_code, app_name, app_url) 
	VALUES (101, 'APP.113', 'Ценные бумаги', 'securities');
INSERT INTO applicationsref (app_id, app_code, app_name, app_url) 
	VALUES (104, 'app.113.4', 'Кредиты нефизическим лицам', 'loan-contracts/bla-bla-bla.xhtml');
INSERT INTO applicationsref (app_id, app_code, app_name, app_url) 
	VALUES (100, 'APP.13', 'Кредиты физическим лицам', 'loan-contracts/гы-гы');
INSERT INTO applicationsref (app_id, app_code, app_name, app_url) 
	VALUES (1001, 'USERS', 'Карточка пользователя', 'security-manager/user.xhtml');
INSERT INTO applicationsref (app_id, app_code, app_name, app_url) 
	VALUES (1100, 'RUNAPP', 'Форма запуска приложений', 'security-manager/run-application-form.xhtml');
INSERT INTO applicationsref (app_id, app_code, app_name, app_url) 
	VALUES (1002, 'ROLES', 'Пользовательская роль', 'security-manager/role.xhtml');
INSERT INTO applicationsref (app_id, app_code, app_name, app_url) 
	VALUES (102, 'lc.fl', 'Кредиты подозрительным лицам', 'loan-contracts/Loan2Individual.xhtml');
INSERT INTO applicationsref (app_id, app_code, app_name, app_url) 
	VALUES (105, 'LOAN.MASSOPER', 'Массовые операции', 'loan-contracts/massopers-form.xhtml');


INSERT INTO contractsubjectsref (contract_subject_id, contract_subject_name) 
	VALUES (210001, 'Размещение средств в форме кредита физическим лицам');
INSERT INTO contractsubjectsref (contract_subject_id, contract_subject_name) 
	VALUES (210002, 'Размещение средств в форме займа физическим лицам');

INSERT INTO entitykindsref (entity_kind_id, entity_type_id, entity_kind_name) 
	VALUES (21000010, 2100, 'Кредит ФЛ с выдачей на карточку');
INSERT INTO entitykindsref (entity_kind_id, entity_type_id, entity_kind_name) 
	VALUES (21000020, 2100, 'Кредит ФЛ в форме овердрафта');
INSERT INTO entitykindsref (entity_kind_id, entity_type_id, entity_kind_name) 
	VALUES (21000030, 2100, 'Кредит сотруднику банка');

INSERT INTO entitystatusesref (entity_status_id, entity_type_id, entity_status_name) 
	VALUES (1, 100, 'Действующий пользователь');
INSERT INTO entitystatusesref (entity_status_id, entity_type_id, entity_status_name) 
	VALUES (-1, 100, 'Блокированный пользователь');
INSERT INTO entitystatusesref (entity_status_id, entity_type_id, entity_status_name) 
	VALUES (1, 101, 'Действующая роль');
INSERT INTO entitystatusesref (entity_status_id, entity_type_id, entity_status_name) 
	VALUES (-1, 101, 'Аннулированная роль');
INSERT INTO entitystatusesref (entity_status_id, entity_type_id, entity_status_name) 
	VALUES (0, 2100, 'Действующий договор');
INSERT INTO entitystatusesref (entity_status_id, entity_type_id, entity_status_name) 
	VALUES (1, 2100, 'Закрытый договор');
INSERT INTO entitystatusesref (entity_status_id, entity_type_id, entity_status_name) 
	VALUES (1, 2200, 'Закрытый договор');
INSERT INTO entitystatusesref (entity_status_id, entity_type_id, entity_status_name) 
	VALUES (0, 2200, 'Действующий договор');


INSERT INTO entitytypesref (entity_type_id, entity_type_name) 
	VALUES (100, 'Пользователь системы');
INSERT INTO entitytypesref (entity_type_id, entity_type_name) 
	VALUES (101, 'Пользовательская роль');
INSERT INTO entitytypesref (entity_type_id, entity_type_name) 
	VALUES (2100, 'Кредит физическому лицу');
INSERT INTO entitytypesref (entity_type_id, entity_type_name) 
	VALUES (2200, 'Кредит юридическому лицу');
INSERT INTO entitytypesref (entity_type_id, entity_type_name) 
	VALUES (2300, 'Межбанковский кредит');
INSERT INTO entitytypesref (entity_type_id, entity_type_name) 
	VALUES (2400, 'Кредит от правительств других стран');

INSERT INTO liasactiontypesref (action_type_id, change_rest_tag, action_type_name) 
	VALUES (1, true, 'Принятие к учету обязательства');
INSERT INTO liasactiontypesref (action_type_id, change_rest_tag, action_type_name) 
	VALUES (2, true, 'Ввод остатков по учтенным обязательствам');
INSERT INTO liasactiontypesref (action_type_id, change_rest_tag, action_type_name) 
	VALUES (3, true, 'Снятие с учета обязательства');
INSERT INTO liasactiontypesref (action_type_id, change_rest_tag, action_type_name) 
	VALUES (4, true, 'Уменьшение суммы обязательства');
INSERT INTO liasactiontypesref (action_type_id, change_rest_tag, action_type_name) 
	VALUES (5, false, 'Связывание обязательств');
INSERT INTO liasactiontypesref (action_type_id, change_rest_tag, action_type_name) 
	VALUES (6, false, 'Открепление связи по обязательствам');
INSERT INTO liasactiontypesref (action_type_id, change_rest_tag, action_type_name) 
	VALUES (7, true, 'Увеличение суммы обязательства');
INSERT INTO liasactiontypesref (action_type_id, change_rest_tag, action_type_name) 
	VALUES (8, false, 'Редактирование атрибутов обязательства');
INSERT INTO liasactiontypesref (action_type_id, change_rest_tag, action_type_name) 
	VALUES (9, true, 'Клирингование обязательств');

INSERT INTO liasbaseassettypesref (base_asset_type_id, base_asset_type_name) 
	VALUES (1, 'Денежные средства');
INSERT INTO liasbaseassettypesref (base_asset_type_id, base_asset_type_name) 
	VALUES (2, 'Валютные ценности');
INSERT INTO liasbaseassettypesref (base_asset_type_id, base_asset_type_name) 
	VALUES (3, 'Ценные бумаги');
INSERT INTO liasbaseassettypesref (base_asset_type_id, base_asset_type_name) 
	VALUES (4, 'Драгоценные металлы');
INSERT INTO liasbaseassettypesref (base_asset_type_id, base_asset_type_name) 
	VALUES (5, 'Кредитные ресурсы');
INSERT INTO liasbaseassettypesref (base_asset_type_id, base_asset_type_name) 
	VALUES (6, 'Недвижимость (кроме жилья)');
INSERT INTO liasbaseassettypesref (base_asset_type_id, base_asset_type_name) 
	VALUES (7, 'Жилые помещения');
INSERT INTO liasbaseassettypesref (base_asset_type_id, base_asset_type_name) 
	VALUES (8, 'Имущество');
INSERT INTO liasbaseassettypesref (base_asset_type_id, base_asset_type_name) 
	VALUES (9, 'Фондовые индексы');
INSERT INTO liasbaseassettypesref (base_asset_type_id, base_asset_type_name) 
	VALUES (10, 'Процентные ставки');
INSERT INTO liasbaseassettypesref (base_asset_type_id, base_asset_type_name) 
	VALUES (11, 'Незавершенное строительство (жилье)');
INSERT INTO liasbaseassettypesref (base_asset_type_id, base_asset_type_name) 
	VALUES (12, 'Имущественные права');
INSERT INTO liasbaseassettypesref (base_asset_type_id, base_asset_type_name) 
	VALUES (13, 'Дебиторская задолженность');
INSERT INTO liasbaseassettypesref (base_asset_type_id, base_asset_type_name) 
	VALUES (14, 'Услуги');

INSERT INTO liasdebtstatesref (debt_state_id, debt_state_name) 
	VALUES (1, 'Нормальная задолженность ');
INSERT INTO liasdebtstatesref (debt_state_id, debt_state_name) 
	VALUES (2, 'Единожды пролонгированная задолженность ');
INSERT INTO liasdebtstatesref (debt_state_id, debt_state_name) 
	VALUES (3, 'Неоднократно пролонгированная задолженность ');
INSERT INTO liasdebtstatesref (debt_state_id, debt_state_name) 
	VALUES (4, 'Просроченная задолженность ');
INSERT INTO liasdebtstatesref (debt_state_id, debt_state_name) 
	VALUES (5, 'Не востребованная в срок задолженность ');
INSERT INTO liasdebtstatesref (debt_state_id, debt_state_name) 
	VALUES (6, 'Безнадежная  задолженность ');
INSERT INTO liasdebtstatesref (debt_state_id, debt_state_name) 
	VALUES (7, 'Задолженность до восстребования');
INSERT INTO liasdebtstatesref (debt_state_id, debt_state_name) 
	VALUES (61, 'Проблемная текущая задолженность');
INSERT INTO liasdebtstatesref (debt_state_id, debt_state_name) 
	VALUES (62, 'Проблемная пролонгированная задолженность');
INSERT INTO liasdebtstatesref (debt_state_id, debt_state_name) 
	VALUES (63, 'Проблемная просроченная задолженность ');

INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (0, 'Операции не отражаемые в плоскости учета срочных обязательств ');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (1, 'Ввод остатков по обязательствам ');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (2, 'Размещение средств ');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (3, 'Привлечение средств');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (4, 'Начисление процентных и комиссионных доходов');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (5, 'Начисление процентных и комиссионных расходов банка');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (6, 'Списание излишне причисленных процентов (дисконта)');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (7, 'Списание излишне причисленных процентов (дисконта)');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (9, 'Возврат размещенных средств');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (10, 'Возврат привлеченных средств');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (11, 'Поступление процентных и прочих доходов');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (12, 'Выплата процентного и комиссионного вознаграждения');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (13, 'Вынос на просрочку задолженности по размещенным средствам)');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (14, 'Пролонгация задолженности по размещенным средствам)');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (15, 'Дополнительный взнос (довнесение средств))');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (16, 'Капитализация процентов');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (17, 'Урегулирование начисленных процентных и прочих доходов');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (18, 'Платеж по выданным гарантиям');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (20, 'Неподтвержденные платежи в расчетах по сделкам');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (21, 'Реструктуризация задолженности');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (22, 'Пролонгация задолженности на условиях "Ролловер"');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (23, 'Подтверждение исполнения платежей по сделкам');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (24, 'Переоформление задолженности по привлеченным средствам');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (25, 'Размещение средств с погашением существующей задолженности');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (26, 'Уменьшение суммы обязательства');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (27, 'Увеличение суммы обязательства');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (28, 'Переоформление задолженности без движения по счетам');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (29, 'Перенос кредиторской задолженности на доходы будущих периодов');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (30, 'Исполнение обязательств в счет погашения требований');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (31, 'Признание доходов отчетного периода');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (32, 'Восстановление суммы дисконта');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (33, 'Погашение задолженности за счет созданного резерва');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (34, 'Вложения в ценные бумаги');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (35, 'Погашение ЦБ клиентом');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (36, 'Продажа собственных ценных бумаг');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (37, 'Выкуп собственных ценных бумаг');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (38, 'Увеличение стоимости при накопленной положительной переоценке ЦБ');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (39, 'Уменьшение стоимости при накопленной отрицательной переоценке ЦБ');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (40, 'Уменьшение стоимости при накопленной положительной переоценке ЦБ');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (41, 'Признание кредиторской задолженности');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (42, 'Погашение кредиторской задолженности');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (43, 'Признание дебиторской задолженности');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (44, 'Погашение дебиторской задолженности');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (45, 'Размещение средств на условиях РЕПО');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (46, 'Отнесение задолженности на расходы будущих периодов');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (47, 'Признание расходов отчетного периода');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (48, 'Привлечение средств на условиях РЕПО');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (49, 'Причисление суммы дисконта');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (50, 'Вынос на просрочку задолженности по уплате вознаграждения банку');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (51, 'Перенос дебиторской задолженности на другой счет');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (52, 'Уплата налогов и сборов');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (53, 'Удержание налогов и сборов');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (54, 'Размещение средств под обязательство в расчетах');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (55, 'Погашение задолженности в счет обязательства в расчетах');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (56, 'Перенос долга на другой счет по активным операциям');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (57, 'Перенос долга на другой счет по пассивным операциям');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (58, 'Привлечение средств по обязательствам в расчетах');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (59, 'Перечисление средств полученных в обеспечение исполнения обязате');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (60, 'Признание гарантийного фонда');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (61, 'Погашение задолженности за счет средств в расчетах');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (62, 'Предварительное зачисление поступивших средств в депозиты');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (63, 'Погашение задолженности за счет поступивших средст в расчетах');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (64, 'Зачисление сумм по подветржденным расчетам');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (65, 'Принятие к учете размещенных средств на временной основе');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (66, 'Списание депонированных средств по исполненным обязательствам ба');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (67, 'Размещение средств с использованием промежуточных счетов');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (68, 'Возврат привлеченных средств с использованием промежуточных счет');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (69, 'Неттингование задолженности по дебиторам и кредиторам');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (70, 'Увеличение резервов в связи с изменением качества задолженности');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (71, 'Увеличение резервов в связи с изменением официального курса');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (80, 'Уменьшение резервов в связи с исполнением строной обязательств');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (81, 'Уменьшение резервов в связи с изменением качества задолженности');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (82, 'Уменьшение резервов в связи с изменением официального курса');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (83, 'Уменьшение резервов по истечению срока действия обязательства');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (85, 'Увеличение стоимости при накопленной отрицательной переоценке ЦБ');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (86, 'Списание начисленного купонного дохода');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (87, 'Выкуп собственных ценных бумаг в счет обязательств в расчетах');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (88, 'Вынос на просрочку задолженности по купонному платежу');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (91, 'Начисление расходов по услугам');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (93, 'Начисление доходов по услугам');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (101, 'Принятие к учету условного обязательства');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (102, 'Уменьшение суммы условного обязательства');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (103, 'Увеличение суммы условного обязательства');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (104, 'Уменьшение суммы в связи с погашением задолженности');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (105, 'Снятие с учета обязательства');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (106, 'Списание проблемной задолженности по неполученным доходам');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (107, 'Списание обязательств исполненных клиентом');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (108, 'Снятие с учета безнадежной задолженности');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (110, 'Принятие к учету списанного с баланса остатка задолженности');
INSERT INTO liasfinopercodesref (fin_oper_code, fin_oper_name) 
	VALUES (111, 'Вынос на просрочку проблемной задолженности');

INSERT INTO liasgroupsref (lias_group_id, lias_group_name) 
	VALUES (1, 'Обязательства денежного рынка');
INSERT INTO liasgroupsref (lias_group_id, lias_group_name) 
	VALUES (2, 'Обязательства по кредитованию клиентов');
INSERT INTO liasgroupsref (lias_group_id, lias_group_name) 
	VALUES (3, 'Обязательства по привлечению средств клиентов в депозиты');
INSERT INTO liasgroupsref (lias_group_id, lias_group_name) 
	VALUES (4, 'Обязательства валютного рынка');
INSERT INTO liasgroupsref (lias_group_id, lias_group_name) 
	VALUES (5, 'Обязательства по обеспечению ');
INSERT INTO liasgroupsref (lias_group_id, lias_group_name) 
	VALUES (6, 'Обязательства по платности');
INSERT INTO liasgroupsref (lias_group_id, lias_group_name) 
	VALUES (7, 'Обязательства по резервам');
INSERT INTO liasgroupsref (lias_group_id, lias_group_name) 
	VALUES (8, 'Обязательства фондового рынка');
INSERT INTO liasgroupsref (lias_group_id, lias_group_name) 
	VALUES (9, 'Долевое участие');
INSERT INTO liasgroupsref (lias_group_id, lias_group_name) 
	VALUES (10, 'Обязательства по посредническим операциям');
INSERT INTO liasgroupsref (lias_group_id, lias_group_name) 
	VALUES (11, 'Обязательства по хозяйственной деятельности банка');

INSERT INTO liaskindsref (lias_kind_id, is_claim, lias_group_id, lias_kind_name) 
	VALUES (1, true, 1, 'Требования по возврату базисного актива ');
INSERT INTO liaskindsref (lias_kind_id, is_claim, lias_group_id, lias_kind_name) 
	VALUES (2, false, 1, 'Обязательства по возврату базисного актива');
INSERT INTO liaskindsref (lias_kind_id, is_claim, lias_group_id, lias_kind_name) 
	VALUES (3, true, 6, 'Требования по уплате процентов');
INSERT INTO liaskindsref (lias_kind_id, is_claim, lias_group_id, lias_kind_name) 
	VALUES (4, false, 6, 'Обязательства по уплате процентов');
INSERT INTO liaskindsref (lias_kind_id, is_claim, lias_group_id, lias_kind_name) 
	VALUES (5, false, 7, 'Специальные резервы на покрытие возможных убытков по активам, подверженным кредитному риску');
INSERT INTO liaskindsref (lias_kind_id, is_claim, lias_group_id, lias_kind_name) 
	VALUES (6, false, 1, 'Обязательства по возврату централизованных ресурсов');
INSERT INTO liaskindsref (lias_kind_id, is_claim, lias_group_id, lias_kind_name) 
	VALUES (7, true, 1, 'Обязательства банка по возврату субординированных кредитов');
INSERT INTO liaskindsref (lias_kind_id, is_claim, lias_group_id, lias_kind_name) 
	VALUES (8, true, 1, 'Исполненные банком обязательства по обеспечению');
INSERT INTO liaskindsref (lias_kind_id, is_claim, lias_group_id, lias_kind_name) 
	VALUES (9, true, 2, 'Требования по обязательным резервам');
INSERT INTO liaskindsref (lias_kind_id, is_claim, lias_group_id, lias_kind_name) 
	VALUES (10, true, 2, 'Платежные документы, приобретенные для продажи');

INSERT INTO liastypesref (lias_type_id, lias_type_name) 
	VALUES (1, 'Начальные обязательства (Обязательства по предоставлению базисного актива) ');
INSERT INTO liastypesref (lias_type_id, lias_type_name) 
	VALUES (2, 'Текущие обязательства (Обязательства по возврату базисного актива) ');
INSERT INTO liastypesref (lias_type_id, lias_type_name) 
	VALUES (3, 'Акцессорные обязательства (Обязательства по исполнению) ');
INSERT INTO liastypesref (lias_type_id, lias_type_name) 
	VALUES (4, 'Обязательства по платности (Зависимые обязательства)  ');
INSERT INTO liastypesref (lias_type_id, lias_type_name) 
	VALUES (5, 'Обязательства по резервам (Зависимые обязательства)   ');

INSERT INTO loansourcesref (loan_source_id, loan_source_name) 
	VALUES (100, 'Средства банка');
INSERT INTO loansourcesref (loan_source_id, loan_source_name) 
	VALUES (101, 'Собственные средства банка');
INSERT INTO loansourcesref (loan_source_id, loan_source_name) 
	VALUES (102, 'Привлеченные ресурсы банка');
INSERT INTO loansourcesref (loan_source_id, loan_source_name) 
	VALUES (200, 'Средства бюджетов');
INSERT INTO loansourcesref (loan_source_id, loan_source_name) 
	VALUES (201, 'Средства местного бюджета');
INSERT INTO loansourcesref (loan_source_id, loan_source_name) 
	VALUES (202, 'Средства республиканского бюджета');
INSERT INTO loansourcesref (loan_source_id, loan_source_name) 
	VALUES (300, 'Централизованные кредитные ресурсы');
INSERT INTO loansourcesref (loan_source_id, loan_source_name) 
	VALUES (400, 'Средства Центрального банка');
INSERT INTO loansourcesref (loan_source_id, loan_source_name) 
	VALUES (500, 'Средства спец.фондов');
INSERT INTO loansourcesref (loan_source_id, loan_source_name) 
	VALUES (600, 'Средства международных фондов и организаций');


INSERT INTO registryparams (param_id, parent_param_id, param_name, param_value, param_descr) 
	VALUES (1, NULL, 'logSqlQuery', '1', 'Лог sql запросов');
