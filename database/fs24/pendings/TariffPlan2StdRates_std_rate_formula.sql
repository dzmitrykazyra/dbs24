ALTER TABLE TariffPlan2StdRates
ADD COLUMN std_rate_formula TStr200;


ALTER TABLE TariffPlan2StdRates
ALTER COLUMN std_rate_formula SET NOT NULL;
