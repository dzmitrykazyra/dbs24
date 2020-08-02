ALTER TABLE CurrenciesRef
RENAME COLUMN currency_code TO currency_id;

ALTER TABLE EntityContracts
RENAME COLUMN currency_code TO currency_id;

ALTER TABLE TariffRates_1
RENAME COLUMN currency_code TO currency_id;

ALTER TABLE TariffRates_2
RENAME COLUMN currency_code TO currency_id;

ALTER TABLE TariffRates_3
RENAME COLUMN currency_code TO currency_id;

ALTER TABLE LiasDebts
RENAME COLUMN currency_code TO currency_id;
