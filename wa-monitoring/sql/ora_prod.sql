select count(*)/1000000 from  wa_tracker.visitNote;
select count(*) from  wa_tracker.appUser;
select count(*) from  wa_tracker.subscriptionPhone;
select count(*) from  wa_tracker.authKey;
select count(*) from  wa_tracker.payment;
select * from  wa_tracker.payment where pay_type<>'trial' and cur_code='usd' and price>0 order by  id desc;

select sum(price) from  wa_tracker.payment where pay_type<>'trial' and cur_code='usd' and price>0 and fulfil_time >=to_date('01.02.2021', 'DD.MM.YYYY');

select * from  wa_tracker.payment  order by  id desc;

select * from  wa_tracker.appUser order by id
select * from  wa_tracker.subscriptionPhone order by id desc


select p.subs_amount, count(*)
  from  wa_tracker.payment p
    group by p.subs_amount

select p.pay_type, count(*)
  from  wa_tracker.payment p
    group by p.pay_type


select p.*
  from  wa_tracker.payment p
  where p.subs_amount in (2,6,11)


select curr_code, count(*) cnt
  from WA_TRACKER.payment
  group by curr_code
  order by cnt desc



