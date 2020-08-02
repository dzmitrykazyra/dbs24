CREATE OR REPLACE FUNCTION tariffs_insertorupdate_tariffgroup(
	p_tariff_group_id tidcode,
	p_tariff_group_name tstr100)
    RETURNS void
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$

begin 
    
INSERT INTO tariffgroupsref (tariff_group_id, tariff_group_name) 
	VALUES (p_tariff_group_id, p_tariff_group_name)
      ON CONFLICT (tariff_group_id) DO
    UPDATE
      SET tariff_group_name = EXCLUDED.tariff_group_name;

end

$BODY$;