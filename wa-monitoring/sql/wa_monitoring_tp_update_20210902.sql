/*==============================================================*/
-- contract_types
update wa_users_contracts set contract_type_id=30 where contract_type_id=10;
update wa_users_contracts set contract_type_id=10 where contract_type_id=1;
update wa_users_contracts set contract_type_id=20 where contract_type_id=4;
update wa_users_contracts set contract_type_id=100 where contract_type_id=99;

update wa_users_contracts_hist set contract_type_id=30 where contract_type_id=10;
update wa_users_contracts_hist set contract_type_id=10 where contract_type_id=1;
update wa_users_contracts_hist set contract_type_id=20 where contract_type_id=4;
update wa_users_contracts_hist set contract_type_id=100 where contract_type_id=99;

delete from wa_contract_types_ref where contract_type_id in (1,4,99);

-- contract_statuses
update wa_users_contracts set contract_status_id=0 where contract_status_id=9;
update wa_users_contracts set contract_status_id=-1 where contract_status_id=2;
update wa_users_contracts set contract_status_id=-1 where contract_status_id=8;

update wa_users_contracts_hist set contract_status_id=0 where contract_status_id=9;
update wa_users_contracts_hist set contract_status_id=-1 where contract_status_id=2;
update wa_users_contracts_hist set contract_status_id=-1 where contract_status_id=8;

delete from wa_contract_statuses_ref where contract_status_id not in (-1, 0, 1);

delete from wa_uss_activities where subscription_id in (select subscription_id from wa_users_subscriptions where subscription_status_id in (-3));
delete from wa_users_subscriptions_hist where subscription_status_id in (-3);
delete from wa_users_subscriptions where subscription_status_id in (-3);
delete from wa_subscription_statuses_ref where subscription_status_id in (-3);

-- commit

commit;