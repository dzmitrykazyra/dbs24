/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     8/30/2021 5:29:15 PM                         */
/*==============================================================*/


drop index pmt_idx_orig_tr_id;

drop table pmt_apple_payments;

drop index pmt_idx_orig_tr_id2;

drop table pmt_apple_payments_hist;

drop table pmt_applications_ref;

drop table pmt_countries_ref;

drop table pmt_currencies_ref;

drop index pmt_idx_purch_token;

drop table pmt_google_payments;

drop index pmt_idx_purch_token2;

drop table pmt_google_payments_hist;

drop table pmt_payers;

drop table pmt_payment_services_ref;

drop table pmt_payment_statuses_ref;

drop table pmt_payments;

drop table pmt_payments_hist;

drop domain TBanknotesAmt;

drop domain TBinary;

drop domain TBoolean;

drop domain TChessGameScore;

drop domain TCoinsAmt;

drop domain TCurrencyISO;

drop domain TCurrencyStr10;

drop domain TDate;

drop domain TDateTime;

drop domain TGPSCoordinates;

drop domain TIdBigCode;

drop domain TIdCode;

drop domain TIdUser;

drop domain TImage;

drop domain TIntCounter;

drop domain TInteger;

drop domain TItemType;

drop domain TMoney;

drop domain TPercRate;

drop domain TPercRateExt;

drop domain TReal;

drop domain TStr10;

drop domain TStr100;

drop domain TStr10000;

drop domain TStr128;

drop domain TStr2;

drop domain TStr20;

drop domain TStr200;

drop domain TStr2000;

drop domain TStr3;

drop domain TStr30;

drop domain TStr40;

drop domain TStr400;

drop domain TStr50;

drop domain TStr80;

drop domain TSumExt;

drop domain TText;

drop domain TTime;

drop domain TidByteCode;

drop sequence seq_pmt_Payers;

drop sequence seq_pmt_Payments;

create sequence seq_pmt_Payers
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

create sequence seq_pmt_Payments
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

/*==============================================================*/
/* Domain: TBanknotesAmt                                        */
/*==============================================================*/
create domain TBanknotesAmt as INTEGER;

/*==============================================================*/
/* Domain: TBinary                                              */
/*==============================================================*/
create domain TBinary as bytea;

/*==============================================================*/
/* Domain: TBoolean                                             */
/*==============================================================*/
create domain TBoolean as BOOL;

/*==============================================================*/
/* Domain: TChessGameScore                                      */
/*==============================================================*/
create domain TChessGameScore as DECIMAL(3,1);

/*==============================================================*/
/* Domain: TCoinsAmt                                            */
/*==============================================================*/
create domain TCoinsAmt as INTEGER;

/*==============================================================*/
/* Domain: TCurrencyISO                                         */
/*==============================================================*/
create domain TCurrencyISO as VARCHAR(3);

/*==============================================================*/
/* Domain: TCurrencyStr10                                       */
/*==============================================================*/
create domain TCurrencyStr10 as VARCHAR(10);

/*==============================================================*/
/* Domain: TDate                                                */
/*==============================================================*/
create domain TDate as DATE;

/*==============================================================*/
/* Domain: TDateTime                                            */
/*==============================================================*/
create domain TDateTime as TIMESTAMP;

/*==============================================================*/
/* Domain: TGPSCoordinates                                      */
/*==============================================================*/
create domain TGPSCoordinates as POINT;

/*==============================================================*/
/* Domain: TIdBigCode                                           */
/*==============================================================*/
create domain TIdBigCode as BIGINT;

/*==============================================================*/
/* Domain: TIdCode                                              */
/*==============================================================*/
create domain TIdCode as INTEGER;

comment on domain TIdCode is
'Код сущности';

/*==============================================================*/
/* Domain: TIdUser                                              */
/*==============================================================*/
create domain TIdUser as INTEGER;

/*==============================================================*/
/* Domain: TImage                                               */
/*==============================================================*/
create domain TImage as BYTEA;

/*==============================================================*/
/* Domain: TIntCounter                                          */
/*==============================================================*/
create domain TIntCounter as INTEGER;

/*==============================================================*/
/* Domain: TInteger                                             */
/*==============================================================*/
create domain TInteger as INTEGER;

/*==============================================================*/
/* Domain: TItemType                                            */
/*==============================================================*/
create domain TItemType as SMALLINT;

/*==============================================================*/
/* Domain: TMoney                                               */
/*==============================================================*/
create domain TMoney as NUMERIC(22,4);

/*==============================================================*/
/* Domain: TPercRate                                            */
/*==============================================================*/
create domain TPercRate as NUMERIC(12,6);

/*==============================================================*/
/* Domain: TPercRateExt                                         */
/*==============================================================*/
create domain TPercRateExt as NUMERIC(16,8);

/*==============================================================*/
/* Domain: TReal                                                */
/*==============================================================*/
create domain TReal as real;

/*==============================================================*/
/* Domain: TStr10                                               */
/*==============================================================*/
create domain TStr10 as VARCHAR(10);

/*==============================================================*/
/* Domain: TStr100                                              */
/*==============================================================*/
create domain TStr100 as VARCHAR(100);

/*==============================================================*/
/* Domain: TStr10000                                            */
/*==============================================================*/
create domain TStr10000 as VARCHAR(10000);

/*==============================================================*/
/* Domain: TStr128                                              */
/*==============================================================*/
create domain TStr128 as VARCHAR(128);

/*==============================================================*/
/* Domain: TStr2                                                */
/*==============================================================*/
create domain TStr2 as VARCHAR(2);

/*==============================================================*/
/* Domain: TStr20                                               */
/*==============================================================*/
create domain TStr20 as VARCHAR(20);

/*==============================================================*/
/* Domain: TStr200                                              */
/*==============================================================*/
create domain TStr200 as VARCHAR(200);

/*==============================================================*/
/* Domain: TStr2000                                             */
/*==============================================================*/
create domain TStr2000 as VARCHAR(2000);

/*==============================================================*/
/* Domain: TStr3                                                */
/*==============================================================*/
create domain TStr3 as VARCHAR(3);

/*==============================================================*/
/* Domain: TStr30                                               */
/*==============================================================*/
create domain TStr30 as VARCHAR(30);

/*==============================================================*/
/* Domain: TStr40                                               */
/*==============================================================*/
create domain TStr40 as VARCHAR(40);

/*==============================================================*/
/* Domain: TStr400                                              */
/*==============================================================*/
create domain TStr400 as VARCHAR(400);

/*==============================================================*/
/* Domain: TStr50                                               */
/*==============================================================*/
create domain TStr50 as VARCHAR(50);

/*==============================================================*/
/* Domain: TStr80                                               */
/*==============================================================*/
create domain TStr80 as VARCHAR(80);

/*==============================================================*/
/* Domain: TSumExt                                              */
/*==============================================================*/
create domain TSumExt as NUMERIC(24,4);

/*==============================================================*/
/* Domain: TText                                                */
/*==============================================================*/
create domain TText as TEXT;

/*==============================================================*/
/* Domain: TTime                                                */
/*==============================================================*/
create domain TTime as TIME;

/*==============================================================*/
/* Domain: TidByteCode                                          */
/*==============================================================*/
create domain TidByteCode as NUMERIC(1);

/*==============================================================*/
/* Table: pmt_apple_payments                                    */
/*==============================================================*/
create table pmt_apple_payments (
   payment_id           TIdCode              not null,
   apple_transaction_id TStr200              null,
   apple_original_transaction_id TStr200              null,
   apple_product_id     TStr200              null,
   constraint PK_PMT_APPLE_PAYMENTS primary key (payment_id),
   constraint AK_APPL_PMTS unique (apple_transaction_id)
);

comment on table pmt_apple_payments is
'Пользовательские платежи apple';

/*==============================================================*/
/* Index: pmt_idx_orig_tr_id                                    */
/*==============================================================*/
create  index pmt_idx_orig_tr_id on pmt_apple_payments (
apple_original_transaction_id
);

/*==============================================================*/
/* Table: pmt_apple_payments_hist                               */
/*==============================================================*/
create table pmt_apple_payments_hist (
   payment_id           TIdCode              not null,
   actual_date          TDateTime            not null,
   apple_transaction_id TStr200              null,
   apple_original_transaction_id TStr200              null,
   apple_product_id     TStr200              null
);

comment on table pmt_apple_payments_hist is
'Пользовательские платежи apple - hist';

/*==============================================================*/
/* Index: pmt_idx_orig_tr_id2                                   */
/*==============================================================*/
create  index pmt_idx_orig_tr_id2 on pmt_apple_payments_hist (
apple_original_transaction_id
);

/*==============================================================*/
/* Table: pmt_applications_ref                                  */
/*==============================================================*/
create table pmt_applications_ref (
   application_id       TIdCode              not null,
   application_name     TStr100              not null,
   constraint PK_PMT_APPLICATIONS_REF primary key (application_id)
);

/*==============================================================*/
/* Table: pmt_countries_ref                                     */
/*==============================================================*/
create table pmt_countries_ref (
   country_iso          TStr2                not null,
   country_id           TIdCode              not null,
   country_name         TStr100              not null,
   constraint PK_PMT_COUNTRIES_REF primary key (country_iso),
   constraint AK_KEY_COUNTRY_ID unique (country_id)
);

comment on table pmt_countries_ref is
'Коды стран';

/*==============================================================*/
/* Table: pmt_currencies_ref                                    */
/*==============================================================*/
create table pmt_currencies_ref (
   currency_iso         TCurrencyISO         not null,
   currency_id          TIdCode              not null,
   currency_name        TStr100              not null,
   constraint PK_PMT_CURRENCIES_REF primary key (currency_iso),
   constraint AK_KEY_CURRENCY_ID unique (currency_id)
);

/*==============================================================*/
/* Table: pmt_google_payments                                   */
/*==============================================================*/
create table pmt_google_payments (
   payment_id           TIdCode              not null,
   google_order_id      TStr200              null,
   google_purchase_token TStr200              null,
   google_sku           TStr200              null,
   constraint PK_PMT_GOOGLE_PAYMENTS primary key (payment_id),
   constraint AK_GOOGLE_PMT unique (google_order_id)
);

comment on table pmt_google_payments is
'Пользовательские платежи google pay';

/*==============================================================*/
/* Index: pmt_idx_purch_token                                   */
/*==============================================================*/
create  index pmt_idx_purch_token on pmt_google_payments (
google_purchase_token
);

/*==============================================================*/
/* Table: pmt_google_payments_hist                              */
/*==============================================================*/
create table pmt_google_payments_hist (
   payment_id           TIdCode              not null,
   actual_date          TDateTime            not null,
   google_purchase_token TStr200              null,
   google_order_id      TStr200              null,
   google_sku           TStr200              null
);

comment on table pmt_google_payments_hist is
'Пользовательские платежи gp - hist';

/*==============================================================*/
/* Index: pmt_idx_purch_token2                                  */
/*==============================================================*/
create  index pmt_idx_purch_token2 on pmt_google_payments_hist (
google_purchase_token
);

/*==============================================================*/
/* Table: pmt_payers                                            */
/*==============================================================*/
create table pmt_payers (
   payer_id             TIdCode              not null,
   application_id       TIdCode              not null,
   login_token          TStr100              not null,
   constraint PK_PMT_PAYERS primary key (payer_id, application_id),
   constraint AK_LOGIN_TOKEN unique (login_token)
);

comment on table pmt_payers is
'Плательщик';

/*==============================================================*/
/* Table: pmt_payment_services_ref                              */
/*==============================================================*/
create table pmt_payment_services_ref (
   payment_service_id   TIdCode              not null,
   payment_service_name TStr100              not null,
   constraint PK_PMT_PAYMENT_SERVICES_REF primary key (payment_service_id)
);

comment on table pmt_payment_services_ref is
'Платежный сервис';

/*==============================================================*/
/* Table: pmt_payment_statuses_ref                              */
/*==============================================================*/
create table pmt_payment_statuses_ref (
   payment_status_id    TIdCode              not null,
   payment_status_name  TStr100              not null,
   constraint PK_PMT_PAYMENT_STATUSES_REF primary key (payment_status_id)
);

comment on table pmt_payment_statuses_ref is
'Статус платежа';

/*==============================================================*/
/* Table: pmt_payments                                          */
/*==============================================================*/
create table pmt_payments (
   payment_id           TIdCode              not null,
   actual_date          TDateTime            not null,
   application_id       TIdCode              not null,
   payer_id             TIdCode              not null,
   payment_date         TDateTime            not null,
   payment_status_id    TIdCode              not null,
   payment_service_id   TIdCode              not null,
   currency_iso         TCurrencyISO         not null,
   country_iso          TStr2                null,
   summ                 TMoney               not null,
   summ_micros          TIdBigCode           null,
   platform             TStr40               null,
   package              TStr40               null,
   pmt_note             TStr2000             null,
   constraint PK_PMT_PAYMENTS primary key (payment_id)
);

comment on table pmt_payments is
'Пользовательские платежи';

/*==============================================================*/
/* Table: pmt_payments_hist                                     */
/*==============================================================*/
create table pmt_payments_hist (
   payment_id           TIdCode              not null,
   actual_date          TDateTime            not null,
   application_id       TIdCode              null,
   payer_id             TIdCode              null,
   payment_date         TDateTime            null,
   payment_status_id    TIdCode              null,
   payment_service_id   TIdCode              null,
   currency_iso         TCurrencyISO         null,
   country_iso          TStr2                null,
   summ                 TMoney               null,
   summ_micros          TIdBigCode           null,
   platform             TStr40               null,
   package              TStr40               null,
   pmt_note             TStr2000             null
);

comment on table pmt_payments_hist is
'Пользовательские платежи - hist
';

alter table pmt_apple_payments
   add constraint FK_pmt_apple_pmts_pmt_id foreign key (payment_id)
      references pmt_payments (payment_id)
      on delete restrict on update restrict;

alter table pmt_apple_payments_hist
   add constraint FK_pmt_aph_payment_id foreign key (payment_id)
      references pmt_apple_payments (payment_id)
      on delete restrict on update restrict;

alter table pmt_google_payments
   add constraint FK_pmt_google_pay foreign key (payment_id)
      references pmt_payments (payment_id)
      on delete restrict on update restrict;

alter table pmt_google_payments_hist
   add constraint FK_pmt_gp_payment_id foreign key (payment_id)
      references pmt_google_payments (payment_id)
      on delete restrict on update restrict;

alter table pmt_payers
   add constraint FK_pmt_payments_payer_id foreign key (application_id)
      references pmt_applications_ref (application_id)
      on delete restrict on update restrict;

alter table pmt_payments
   add constraint FK_pmt_payment_service_id foreign key (payment_service_id)
      references pmt_payment_services_ref (payment_service_id)
      on delete restrict on update restrict;

alter table pmt_payments
   add constraint FK_pmt_payments_app_id foreign key (application_id)
      references pmt_applications_ref (application_id)
      on delete restrict on update restrict;

alter table pmt_payments
   add constraint FK_pmt_payments_country foreign key (country_iso)
      references pmt_countries_ref (country_iso)
      on delete restrict on update restrict;

alter table pmt_payments
   add constraint FK_pmt_payments_currency_iso foreign key (currency_iso)
      references pmt_currencies_ref (currency_iso)
      on delete restrict on update restrict;

alter table pmt_payments
   add constraint FK_pmt_payments_payer foreign key (payer_id, application_id)
      references pmt_payers (payer_id, application_id)
      on delete restrict on update restrict;

alter table pmt_payments
   add constraint FK_pmt_payments_status_id foreign key (payment_status_id)
      references pmt_payment_statuses_ref (payment_status_id)
      on delete restrict on update restrict;

alter table pmt_payments_hist
   add constraint FK_pmt_payment_id_hist foreign key (payment_id)
      references pmt_payments (payment_id)
      on delete restrict on update restrict;

