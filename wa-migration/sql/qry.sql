select * from wa_users order by user_id desc;
select * from wa_users_hist order by user_id desc;
select * from wa_agent_statusesref;
select * from wa_agents order by agent_id desc;
select * from wa_agents_hist order by agent_id desc;
select * from wa_contract_typesref;
select * from wa_contract_statusesref;
select * from wa_users_subscriptions order by subscription_id desc;
select * from wa_users_subscriptions_hist order by subscription_id desc;
select * from wa_uss_activities order by actual_date desc;
delete from wa_uss_activities;
select count(*) from wa_uss_activities;
select * from wa_uss_activities order by activity_id desc;
select * from wa_uss_activities order by actual_date desc;