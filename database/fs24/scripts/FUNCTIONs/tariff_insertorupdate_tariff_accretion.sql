
DROP FUNCTION tariff_insertorupdate_tariff_accretion;

CREATE OR REPLACE FUNCTION tariff_insertorupdate_tariff_accretion(
        p_accretion_date tdate, p_tariff_serv_id tidcode, p_tariff_kind_id tidcode, p_contract_id tidBigcode, p_lias_action_id tidBigcode)
    RETURNS tidcode
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE 
AS $BODY$
declare v_id tidcode;
begin 

    v_id = nextval('seq_TariffAccretionsHist');
    
INSERT INTO tariffaccretionshist (id, accretion_date, tariff_serv_id, tariff_kind_id, contract_id, lias_action_id) 
	VALUES (v_id, p_accretion_date, p_tariff_serv_id, p_tariff_kind_id, p_contract_id, p_lias_action_id)
      ON CONFLICT (id) DO
    UPDATE
      SET accretion_date = EXCLUDED.accretion_date,
        tariff_serv_id = EXCLUDED.tariff_serv_id,
        tariff_kind_id = EXCLUDED.tariff_kind_id,
        contract_id = EXCLUDED.contract_id,
        lias_action_id = EXCLUDED.lias_action_id;

    return v_id;

end

$BODY$;

