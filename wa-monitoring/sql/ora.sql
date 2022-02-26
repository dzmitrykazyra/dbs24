select * from AUTHKEY order by id;
select * from AUTHKEY where id=2301 order by id;
select * from PAYMENT;
select * from appUser order by id;
select * from BOTGROUP order by id desc;
select * from SUBSCRIPTIONPHONE order by id desc;


select PHONE_NUM, count(*) cnt from AUTHKEY
  group by PHONE_NUM
--  having count(*)>1
  order by cnt desc;


select group_id, count(*) cnt from AUTHKEY
  group by group_id
--  having count(*)>1
  order by cnt desc;


select key_id, count(*) cnt
  from SUBSCRIPTIONPHONE
  group by key_id
  order by cnt desc


select curr_code, count(*) cnt
  from WA_TRACKER.payment
  group by curr_code
  order by cnt desc