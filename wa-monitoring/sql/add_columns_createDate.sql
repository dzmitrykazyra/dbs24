
alter table wa_users_contracts  add column create_date tdatetime;

alter table wa_users_subscriptions  add column create_date tdatetime;
alter table wa_users_subscriptions  add column last_status_change_date tdatetime;
alter table wa_users_subscriptions_hist  add column last_status_change_date tdatetime;

-- create_date
UPDATE wa_users_subscriptions SET create_date = wa_users_subscriptions.actual_date;

UPDATE wa_users_subscriptions
SET create_date=subquery.actual_date
FROM (select subscription_id, actual_date
      from (
               select subscription_id,
                      actual_date,
                      row_number() over (PARTITION BY subscription_id ORDER BY actual_date asc) rn
               from wa_users_subscriptions_hist) c
      where rn = 1) AS subquery
WHERE wa_users_subscriptions.subscription_id = subquery.subscription_id;


-- last_status_change_date
UPDATE wa_users_subscriptions SET last_status_change_date = wa_users_subscriptions.actual_date;

UPDATE wa_users_subscriptions
SET last_status_change_date=subquery.actual_date
FROM (select subscription_id, actual_date
      from (
               select h.subscription_id,
                      h.actual_date,
                      row_number() over (PARTITION BY h.subscription_id ORDER BY h.actual_date desc) rn
               from wa_users_subscriptions_hist h,
                    wa_users_subscriptions s
               where h.subscription_id = s.subscription_id
                 and h.subscription_status_id <> s.subscription_status_id
           ) c
      where rn = 1) AS subquery
WHERE wa_users_subscriptions.subscription_id = subquery.subscription_id;

-- create_date
UPDATE wa_users_contracts SET create_date = wa_users_contracts.actual_date;

UPDATE wa_users_contracts
SET create_date=subquery.actual_date
FROM (select contract_id, actual_date
      from (
               select contract_id,
                      actual_date,
                      row_number() over (PARTITION BY contract_id ORDER BY actual_date asc) rn
               from wa_users_contracts_hist) c
      where rn = 1) AS subquery
WHERE wa_users_contracts.contract_id = subquery.contract_id;

-- not null
ALTER TABLE wa_users_contracts ALTER COLUMN create_date SET NOT NULL;
ALTER TABLE wa_users_subscriptions ALTER COLUMN last_status_change_date SET NOT NULL;
ALTER TABLE wa_users_subscriptions ALTER COLUMN create_date SET NOT NULL;


commit;