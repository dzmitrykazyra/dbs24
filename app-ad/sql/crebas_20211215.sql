/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     15.12.2021 18:16:57                          */
/*==============================================================*/


drop table ads_applications_ref;

drop index idx_note_ads_date;

drop table ads_common_settings;

drop table ads_common_settings_hist;

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

drop sequence seq_ads;

create sequence seq_ads
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
/* Table: ads_applications_ref                                  */
/*==============================================================*/
create table ads_applications_ref (
   application_id       TInteger             not null,
   application_code     TStr10               not null,
   application_name     TStr128              not null,
   constraint PK_ADS_APPLICATIONS_REF primary key (application_id),
   constraint AK_NOTE_APP_CODE unique (application_code)
);

comment on table ads_applications_ref is
'Используемые приложения';

/*==============================================================*/
/* Table: ads_common_settings                                   */
/*==============================================================*/
create table ads_common_settings (
   setting_id           TIdCode              not null,
   application_id       TIdCode              not null,
   actual_date          TDateTime            not null,
   create_date          TDateTime            not null,
   is_actual            TBoolean             not null,
   packages             TStr200              not null,
   setting_body         TStr10000            not null,
   constraint PK_ADS_COMMON_SETTINGS primary key (setting_id)
);

comment on table ads_common_settings is
'Общие настройки';

/*==============================================================*/
/* Index: idx_note_ads_date                                     */
/*==============================================================*/
create  index idx_note_ads_date on ads_common_settings (
actual_date
);

/*==============================================================*/
/* Table: ads_common_settings_hist                              */
/*==============================================================*/
create table ads_common_settings_hist (
   setting_id           TIdCode              not null,
   actual_date          TDateTime            not null,
   create_date          TDateTime            not null,
   application_id       TIdCode              null,
   is_actual            TBoolean             null,
   setting_body         TStr10000            null,
   packages             TStr200              not null
);

alter table ads_common_settings
   add constraint FK_ads_app_id foreign key (application_id)
      references ads_applications_ref (application_id)
      on delete restrict on update restrict;

alter table ads_common_settings_hist
   add constraint FK_ads_cs_setting_id foreign key (setting_id)
      references ads_common_settings (setting_id)
      on delete restrict on update restrict;

