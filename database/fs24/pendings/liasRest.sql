SELECT * FROM core_entities WHERE entity_id in (SELECT MAX(contract_id) FROM core_entitycontracts) order by entity_id desc LIMIT 200;
SELECT * FROM core_entitycontracts WHERE contract_id in (SELECT MAX(contract_id) FROM core_entitycontracts) order by contract_id desc LIMIT 200;
SELECT * FROM liasdebts WHERE contract_id in (SELECT MAX(contract_id) FROM core_entitycontracts) order by debt_id desc LIMIT 200;
SELECT * FROM liases WHERE debt_id IN (SELECT debt_id FROM liasdebts WHERE contract_id in (SELECT MAX(contract_id) FROM core_entitycontracts)) order by debt_id desc LIMIT 200;
SELECT * FROM liasactions WHERE lias_id IN (SELECT lias_id FROM liases WHERE debt_id IN (SELECT debt_id FROM liasdebts WHERE contract_id in (SELECT MAX(contract_id) FROM core_entitycontracts))) order by lias_action_id desc LIMIT 200;
SELECT * FROM liasdebtrests WHERE debt_id IN (SELECT debt_id FROM liasdebts WHERE contract_id in (SELECT MAX(contract_id) FROM core_entitycontracts)) order by debt_id, rest_date LIMIT 200;
SELECT * FROM liasrests WHERE lias_id IN (SELECT lias_id FROM liases WHERE debt_id IN (SELECT debt_id FROM liasdebts WHERE contract_id in (SELECT MAX(contract_id) FROM core_entitycontracts))) order by lias_id asc, rest_date asc LIMIT 200;
select * from documents  order by doc_id desc;
select * from documents where doc_id in (SELECT doc_id FROM liasactions WHERE lias_id IN (SELECT lias_id FROM liases WHERE debt_id IN (SELECT debt_id FROM liasdebts WHERE contract_id in (SELECT MAX(contract_id) FROM core_entitycontracts))) order by lias_action_id desc) order by doc_id desc LIMIT 200;
SELECT * FROM tariffaccretionshist where contract_id in (SELECT MAX(contract_id) FROM core_entitycontracts) order by contract_id desc LIMIT 200;
select dar.*, da.* 
  from docattrs da, docattrsref dar
  where da.doc_id in (SELECT doc_id FROM liasactions WHERE lias_id IN (SELECT lias_id FROM liases WHERE debt_id IN (SELECT debt_id FROM liasdebts WHERE contract_id in (SELECT MAX(contract_id) FROM core_entitycontracts))) order by lias_action_id desc)
  and da.doc_attr_id = dar.doc_attr_id order by da.doc_id desc, da.doc_attr_id;

select * from v_tables_count;

