
alter table wa_users_subscriptions
    add column custom_avatar_img timage;

alter table wa_users_subscriptions_hist
    add column custom_avatar_img timage;

UPDATE wa_users_subscriptions
SET custom_avatar_img = c_avatar_img
    FROM ( select s.subscription_id, s.avatar_img c_avatar_img
               from wa_users_subscriptions s
               where s.avatar_id=0
           ) AS subquery
WHERE wa_users_subscriptions.subscription_id = subquery.subscription_id;

UPDATE wa_users_subscriptions
  SET avatar_img = null
  WHERE avatar_id=0;

UPDATE wa_users_subscriptions_hist
SET custom_avatar_img = c_avatar_img
    FROM ( select s.subscription_id, s.avatar_img c_avatar_img
               from wa_users_subscriptions s
               where s.avatar_id=0
           ) AS subquery
WHERE wa_users_subscriptions_hist.subscription_id = subquery.subscription_id;

UPDATE wa_users_subscriptions_hist
  SET avatar_img = null
  WHERE avatar_id=0;

commit;