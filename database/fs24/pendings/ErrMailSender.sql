

INSERT INTO core_registryparams (param_id, parent_param_id, param_name, param_value, param_descr) 
	VALUES (1001, null, 'mailReportError', '0', 'Включить рассылку почтой информацию об ошибках приложений');	
INSERT INTO core_registryparams (param_id, parent_param_id, param_name, param_value, param_descr) 
	VALUES (1002, null, 'mailDefaultSender', 'smt_service@rw.by', 'Адрес отправителя по умолчанию');
INSERT INTO core_registryparams (param_id, parent_param_id, param_name, param_value, param_descr) 
	VALUES (1003, null, 'mailHost', 'mail.lwo.by', 'Адрес почтового сервера');
INSERT INTO core_registryparams (param_id, parent_param_id, param_name, param_value, param_descr) 
	VALUES (1004, null, 'mailLogin', 'rwserver', 'Логин почты для рассылки сообщений');
INSERT INTO core_registryparams (param_id, parent_param_id, param_name, param_value, param_descr) 
	VALUES (1005, null, 'mailPassword', 'Qwerfdsa1234', 'Пароль почты для рассылки сообщений');
INSERT INTO core_registryparams (param_id, parent_param_id, param_name, param_value, param_descr) 
	VALUES (1006, null, 'mailPort', '25', 'Порт почтового сервера');
INSERT INTO core_registryparams (param_id, parent_param_id, param_name, param_value, param_descr) 
	VALUES (1007, null, 'mailRecipientsList', 'kazyra_d@lwo.by', 'Список получателей рассылки об ошибках');