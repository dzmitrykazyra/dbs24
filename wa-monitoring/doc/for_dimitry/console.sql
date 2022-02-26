select * FROM SUBSCRIPTIONPHONE order by add_time DESC;
select * FROM AUTHKEY order by id DESC;

select * FROM AUTHKEY order by id DESC;

select * FROM SUBSCRIPTIONPHONE WHERE PHONE_NUM LIKE '375%'
select * FROM BOTGROUP
select * FROM VisitNote  ORDER BY Phone_id ASC, ADD_TIME DESC
select IS_ONLINE, count(*) FROM VisitNote
  GROUP BY  IS_ONLINE


SELECT * FROM JOBS_LOG where job_name<>'DELETE_OLD_VISIT_NOTES' ORDER BY LOG_ID DESC
SELECT * FROM RETRIEVEDCONTACTS

select sp.phone_num, vn.*
  FROM VisitNote vn, SUBSCRIPTIONPHONE sp
  WHERE vn.phone_id = sp.id
  ORDER BY vn.Phone_id ASC, vn.ADD_TIME DESC