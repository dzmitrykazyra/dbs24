
alter table wa_users_subscriptions
    add column avatar_modify_date tdatetime;

alter table wa_users_subscriptions_hist
    add column avatar_modify_date tdatetime;

UPDATE wa_users_subscriptions
SET avatar_modify_date=subquery.actual_date
    FROM ( select s.subscription_id, s.actual_date
               from wa_users_subscriptions s
               where s.avatar_id is not null
           ) AS subquery
WHERE wa_users_subscriptions.subscription_id = subquery.subscription_id;

commit;