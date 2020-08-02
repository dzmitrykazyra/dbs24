INSERT INTO liastypesRef (lias_type_id, lias_type_name) 
	VALUES (1, 'Начальные обязательства (Обязательства по предоставлению базисного актива) '),
                (2, 'Текущие обязательства (Обязательства по возврату базисного актива) '),
                (3, 'Акцессорные обязательства (Обязательства по исполнению) '),
                (4, 'Обязательства по платности (Зависимые обязательства)  '),
                (5, 'Обязательства по резервам (Зависимые обязательства)   ')
    ON CONFLICT (lias_type_id) DO UPDATE SET lias_type_name = EXCLUDED.lias_type_name;

INSERT INTO liasgroupsRef (lias_group_id, lias_group_name) 
	VALUES (1, 'Обязательства денежного рынка'),
        (2, 'Обязательства по кредитованию клиентов'),
        (3, 'Обязательства по привлечению средств клиентов в депозиты'),
        (4, 'Обязательства валютного рынка'),
        (5, 'Обязательства по обеспечению '),
        (6, 'Обязательства по платности'),
        (7, 'Обязательства по резервам'),
        (8, 'Обязательства фондового рынка'),
        (9, 'Долевое участие'),
        (10, 'Обязательства по посредническим операциям'),
        (11, 'Обязательства по хозяйственной деятельности банка')
ON CONFLICT (lias_group_id) DO UPDATE SET lias_group_name = EXCLUDED.lias_group_name;

INSERT INTO liaskindsRef (lias_kind_id, is_claim, lias_group_id, lias_kind_name) 
	VALUES (1, true, 1, 'Требования по возврату базисного актива '),
            (2, false, 1, 'Обязательства по возврату базисного актива'),
            (3, true, 6, 'Требования по уплате процентов'),
            (4, false, 6, 'Обязательства по уплате процентов'),
            (5, false, 7, 'Специальные резервы на покрытие возможных убытков по активам, подверженным кредитному риску'),
            (6, false, 1, 'Обязательства по возврату централизованных ресурсов'),
 (7, true, 1, 'Обязательства банка по возврату субординированных кредитов'),
 (8, true, 1, 'Исполненные банком обязательства по обеспечению'),
(9, true, 2, 'Требования по обязательным резервам'),
(10, true, 2, 'Платежные документы, приобретенные для продажи')
    ON CONFLICT (lias_kind_id) DO UPDATE SET is_claim = EXCLUDED.is_claim, lias_group_id = EXCLUDED.lias_group_id, lias_kind_name = EXCLUDED.lias_kind_name;


INSERT INTO liasfinopercodesRef (fin_oper_code, fin_oper_name) 
	VALUES (0, 'Операции не отражаемые в плоскости учета срочных обязательств '),
               (1, 'Ввод остатков по обязательствам '),
(2, 'Размещение средств '),
(3, 'Привлечение средств'),
(4, 'Начисление процентных и комиссионных доходов'),
(5, 'Начисление процентных и комиссионных расходов банка'),
(6, 'Списание излишне причисленных процентов (дисконта)'),
(7, 'Списание излишне причисленных процентов (дисконта)'),
(9, 'Возврат размещенных средств'),
(10, 'Возврат привлеченных средств'),
(11, 'Поступление процентных и прочих доходов'),
(12, 'Выплата процентного и комиссионного вознаграждения'),
(13, 'Вынос на просрочку задолженности по размещенным средствам)'),
    (14,'Пролонгация задолженности по размещенным средствам)'),
(15,'Дополнительный взнос (довнесение средств))'),
(16,'Капитализация процентов'),
(17,'Урегулирование начисленных процентных и прочих доходов'),
(18,'Платеж по выданным гарантиям'),
(20,'Неподтвержденные платежи в расчетах по сделкам'),
(21,'Реструктуризация задолженности'),
(22,'Пролонгация задолженности на условиях "Ролловер"'),
(23,'Подтверждение исполнения платежей по сделкам'),
(24,'Переоформление задолженности по привлеченным средствам'),
(25,'Размещение средств с погашением существующей задолженности'),
(26,'Уменьшение суммы обязательства'),
(27,'Увеличение суммы обязательства'),
(28,'Переоформление задолженности без движения по счетам'),
(29,'Перенос кредиторской задолженности на доходы будущих периодов'),
(30,'Исполнение обязательств в счет погашения требований'),
(31,'Признание доходов отчетного периода'),
(32,'Восстановление суммы дисконта'),
(33,'Погашение задолженности за счет созданного резерва'),
(34,'Вложения в ценные бумаги'),
(35,'Погашение ЦБ клиентом'),
(36,'Продажа собственных ценных бумаг'),
(37,'Выкуп собственных ценных бумаг'),
(38,'Увеличение стоимости при накопленной положительной переоценке ЦБ'),
(39,'Уменьшение стоимости при накопленной отрицательной переоценке ЦБ'),
(40,'Уменьшение стоимости при накопленной положительной переоценке ЦБ'),
(41,'Признание кредиторской задолженности'),
(42,'Погашение кредиторской задолженности'),
(43,'Признание дебиторской задолженности'),
(44,'Погашение дебиторской задолженности'),
(45,'Размещение средств на условиях РЕПО'),
(46,'Отнесение задолженности на расходы будущих периодов'),
(47,'Признание расходов отчетного периода'),
(48,'Привлечение средств на условиях РЕПО'),
(49,'Причисление суммы дисконта'),
(50,'Вынос на просрочку задолженности по уплате вознаграждения банку'),
(51,'Перенос дебиторской задолженности на другой счет'),
(52,'Уплата налогов и сборов'),
(53,'Удержание налогов и сборов'),
(54,'Размещение средств под обязательство в расчетах'),
(55,'Погашение задолженности в счет обязательства в расчетах'),
(56,'Перенос долга на другой счет по активным операциям'),
(57,'Перенос долга на другой счет по пассивным операциям'),
(58,'Привлечение средств по обязательствам в расчетах'),
(59,'Перечисление средств полученных в обеспечение исполнения обязате'),
(60,'Признание гарантийного фонда'),
(61,'Погашение задолженности за счет средств в расчетах'),
(62,'Предварительное зачисление поступивших средств в депозиты'),
(63,'Погашение задолженности за счет поступивших средст в расчетах'),
(64,'Зачисление сумм по подветржденным расчетам'),
(65,'Принятие к учете размещенных средств на временной основе'),
(66,'Списание депонированных средств по исполненным обязательствам ба'),
(67,'Размещение средств с использованием промежуточных счетов'),
(68,'Возврат привлеченных средств с использованием промежуточных счет'),
(69,'Неттингование задолженности по дебиторам и кредиторам'),
(70,'Увеличение резервов в связи с изменением качества задолженности'),
(71,'Увеличение резервов в связи с изменением официального курса'),
(80,'Уменьшение резервов в связи с исполнением строной обязательств'),
(81,'Уменьшение резервов в связи с изменением качества задолженности'),
(82,'Уменьшение резервов в связи с изменением официального курса'),
(83,'Уменьшение резервов по истечению срока действия обязательства'),
(85,'Увеличение стоимости при накопленной отрицательной переоценке ЦБ'),
(86,'Списание начисленного купонного дохода'),
(87,'Выкуп собственных ценных бумаг в счет обязательств в расчетах'),
(88,'Вынос на просрочку задолженности по купонному платежу'),
(91,'Начисление расходов по услугам'),
(93,'Начисление доходов по услугам'),
(101,'Принятие к учету условного обязательства'),
(102,'Уменьшение суммы условного обязательства'),
(103,'Увеличение суммы условного обязательства'),
(104,'Уменьшение суммы в связи с погашением задолженности'),
(105,'Снятие с учета обязательства'),
(106,'Списание проблемной задолженности по неполученным доходам'),
(107,'Списание обязательств исполненных клиентом'),
(108,'Снятие с учета безнадежной задолженности'),
(110,'Принятие к учету списанного с баланса остатка задолженности'),
(111,'Вынос на просрочку проблемной задолженности')
    ON CONFLICT (fin_oper_code) DO UPDATE SET fin_oper_name = EXCLUDED.fin_oper_name;

INSERT INTO liasdebtstatesRef (debt_state_id, debt_state_name) 
	VALUES (1, 'Нормальная задолженность '),
	 (2, 'Единожды пролонгированная задолженность '),
	 (3, 'Неоднократно пролонгированная задолженность '),
	(4, 'Просроченная задолженность '),
	 (5, 'Не востребованная в срок задолженность '),
	 (6, 'Безнадежная  задолженность '),
	 (7, 'Задолженность до восстребования'),
	 (61, 'Проблемная текущая задолженность'),
	 (62, 'Проблемная пролонгированная задолженность'),
	(63, 'Проблемная просроченная задолженность ')
    ON CONFLICT (debt_state_id) DO UPDATE SET debt_state_name = EXCLUDED.debt_state_name;


INSERT INTO liasactiontypesRef (action_type_id, change_rest_tag, action_type_name) 
	VALUES (1, true, 'Принятие к учету обязательства'),
               (2, true, 'Ввод остатков по учтенным обязательствам'),
                (3, true, 'Снятие с учета обязательства'),
(4, true, 'Уменьшение суммы обязательства'),
(5, false, 'Связывание обязательств'),
(6, false, 'Открепление связи по обязательствам'),
(7, true, 'Увеличение суммы обязательства'),
(8, false, 'Редактирование атрибутов обязательства'),
(9, true, 'Клирингование обязательств')
    ON CONFLICT (action_type_id) DO UPDATE SET change_rest_tag = EXCLUDED.change_rest_tag,
action_type_name = EXCLUDED.action_type_name;


INSERT INTO liasbaseassettypesRef (base_asset_type_id, base_asset_type_name) 
	VALUES (1, 'Денежные средства'),
	(2, 'Валютные ценности'),
(3, 'Ценные бумаги'),
(4, 'Драгоценные металлы'),
(5, 'Кредитные ресурсы'),
(6, 'Недвижимость (кроме жилья)'),
(7, 'Жилые помещения'),
(8, 'Имущество'),
(9, 'Фондовые индексы'),
(10, 'Процентные ставки'),
(11, 'Незавершенное строительство (жилье)'),
(12, 'Имущественные права'),
(13, 'Дебиторская задолженность'),
(14, 'Услуги')
ON CONFLICT (base_asset_type_id) DO UPDATE SET base_asset_type_name = EXCLUDED.base_asset_type_name;
