-- дропаем первичный ключ
ALTER TABLE tariffplan2entitycontract DROP CONSTRAINT pk_entitycontract2tariffplans;

-- восстанавливаем первичный ключ
ALTER TABLE tariffplan2entitycontract ADD constraint pk_entitycontract2tariffplans primary key (contract_id);