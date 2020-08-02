CREATE OR REPLACE FUNCTION liases_insertorupdate_baseassettype(
	p_base_asset_type_id tidcode,
	p_base_asset_type_name tstr100)
    RETURNS void
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$

begin 
    
INSERT INTO liasbaseassettypesref (base_asset_type_id, base_asset_type_name) 
	VALUES (p_base_asset_type_id, p_base_asset_type_name)
      ON CONFLICT (base_asset_type_id) DO
    UPDATE
        SET base_asset_type_name = EXCLUDED.base_asset_type_name;

end

$BODY$;