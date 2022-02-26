ALTER TABLE wa_users_subscriptions RENAME subscription_avatar TO avatar_img;
ALTER TABLE wa_users_subscriptions_hist RENAME subscription_avatar TO avatar_img;
ALTER TABLE wa_users_subscriptions ADD COLUMN avatar_id tidbigcode;
ALTER TABLE wa_users_subscriptions_hist ADD COLUMN avatar_id tidbigcode;