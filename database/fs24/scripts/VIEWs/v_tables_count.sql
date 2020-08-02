drop view v_tables_count;
create or replace view v_tables_count
as
select (select count(*) from core_entities) core_entities,
       (select count(*) from core_entitycontracts) core_entitycontracts,
       (select count(*) from core_actions) core_actions,
       (select count(*) from liasdebts) liasdebts,
       (select count(*) from liasdebtrests) liasdebtrests,
       (select count(*) from liases) liases,
       (select count(*) from liasrests) liasrests,
       (select count(*) from liasactions) liasactions,
       (select count(*) from documents) documents,
       (select count(*) from docattrs) docattrs
  from pg_dual;