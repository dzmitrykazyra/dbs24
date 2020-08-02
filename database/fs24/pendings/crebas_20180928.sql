/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     28.09.2018 17:29:28                          */
/*==============================================================*/


drop table Action2Role;

drop table Actions;

drop table ActionsRef;

drop table Application2Role;

drop table ApplicationsRef;

drop table CurrenciesRef;

drop table Entities;

drop table EntityKindRef;

drop table EntityStatusesRef;

drop table EntityTypeRef;

drop table Function2Role;

drop table FunctionsRef;

drop table RegistryParams;

drop table Roles;

drop table User2Role;

drop table Users;

drop domain TBanknotesAmt;

drop domain TCoinsAmt;

drop domain TCurrencyISO;

drop domain TCurrencyStr10;

drop domain TDate;

drop domain TDateTime;

drop domain TGPSCoordinates;

drop domain TIdCode;

drop domain TIdUser;

drop domain TIntCounter;

drop domain TStr10;

drop domain TStr100;

drop domain TStr20;

drop domain TStr200;

drop domain TStr3;

drop domain TStr30;

drop domain TStr40;

drop domain TStr50;

drop domain TStr80;

drop domain TText;

/*==============================================================*/
/* Domain: TBanknotesAmt                                        */
/*==============================================================*/
create domain TBanknotesAmt as INTEGER;

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
/* Domain: TIntCounter                                          */
/*==============================================================*/
create domain TIntCounter as INTEGER;

/*==============================================================*/
/* Domain: TStr10                                               */
/*==============================================================*/
create domain TStr10 as VARCHAR(10);

/*==============================================================*/
/* Domain: TStr100                                              */
/*==============================================================*/
create domain TStr100 as VARCHAR(100);

/*==============================================================*/
/* Domain: TStr20                                               */
/*==============================================================*/
create domain TStr20 as VARCHAR(20);

/*==============================================================*/
/* Domain: TStr200                                              */
/*==============================================================*/
create domain TStr200 as VARCHAR(200);

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
/* Domain: TStr50                                               */
/*==============================================================*/
create domain TStr50 as VARCHAR(50);

/*==============================================================*/
/* Domain: TStr80                                               */
/*==============================================================*/
create domain TStr80 as VARCHAR(80);

/*==============================================================*/
/* Domain: TText                                                */
/*==============================================================*/
create domain TText as TEXT;

/*==============================================================*/
/* Table: Action2Role                                           */
/*==============================================================*/
create table Action2Role (
   action_ref_id        TIdCode              not null,
   role_id              TIdCode              not null,
   assign_date          TDate                not null,
   constraint PK_ACTION2ROLE primary key (action_ref_id, role_id)
);

comment on table Action2Role is
'Действия присвоенные роли';

/*==============================================================*/
/* Table: Actions                                               */
/*==============================================================*/
create table Actions (
   action_id            TIdCode              not null,
   entity_id            TIdCode              not null,
   actref_id            TIdCode              not null,
   user_id              TIdUser              not null,
   execute_date         TDateTime            not null,
   action_address       TStr40               not null,
   json_state           TText                null,
   constraint PK_ACTIONS primary key (action_id)
);

comment on table Actions is
'Картотека выполненный действий пользователя 
';

/*==============================================================*/
/* Table: ActionsRef                                            */
/*==============================================================*/
create table ActionsRef (
   action_ref_id        TIdCode              not null,
   action_name          TStr80               null,
   app_name             TStr100              not null,
   constraint PK_ACTIONSREF primary key (action_ref_id)
);

comment on table ActionsRef is
'Справочник зарегистрированных регистрированных действий над терминалом';

/*==============================================================*/
/* Table: Application2Role                                      */
/*==============================================================*/
create table Application2Role (
   app_id               TIdCode              not null,
   app_code             TStr30               not null,
   role_id              TIdCode              not null,
   assign_date          TDate                not null,
   constraint PK_APPLICATION2ROLE primary key (app_id, app_code, role_id)
);

comment on table Application2Role is
'Приложения присвоенные роли';

/*==============================================================*/
/* Table: ApplicationsRef                                       */
/*==============================================================*/
create table ApplicationsRef (
   app_id               TIdUser              not null,
   app_code             TStr30               not null,
   app_name             TStr50               not null,
   app_url              TStr100              not null,
   constraint PK_APPLICATIONSREF primary key (app_id, app_code)
);

comment on table ApplicationsRef is
'Приложения пользователей';

/*==============================================================*/
/* Table: CurrenciesRef                                         */
/*==============================================================*/
create table CurrenciesRef (
   currency_code        TIdCode              not null,
   currency_iso         TStr3                not null,
   currency_name        TStr80               not null,
   constraint PK_CURRENCIESREF primary key (currency_code)
);

comment on table CurrenciesRef is
'Справочник валют';

/*==============================================================*/
/* Table: Entities                                              */
/*==============================================================*/
create table Entities (
   entity_id            TIdCode              not null,
   entity_type_id       TIdCode              not null,
   entity_status_id     TIdCode              not null,
   creation_date        TDate                not null,
   close_date           TDate                null,
   last_modify          TDateTime            null,
   constraint PK_ENTITIES primary key (entity_id)
);

comment on table Entities is
'Картотека терминалов самообслуживания';

/*==============================================================*/
/* Table: EntityKindRef                                         */
/*==============================================================*/
create table EntityKindRef (
   entity_kind_id       TIdCode              not null,
   entity_type_id       TIdCode              not null,
   entity_kind_title    TStr100              not null,
   constraint PK_ENTITYKINDREF primary key (entity_kind_id)
);

comment on table EntityKindRef is
'Cправочник видов сущностей';

/*==============================================================*/
/* Table: EntityStatusesRef                                     */
/*==============================================================*/
create table EntityStatusesRef (
   entity_status_id     TIdCode              not null,
   entity_type_id       TIdCode              not null,
   entity_status_name   TStr30               not null,
   constraint PK_ENTITYSTATUSESREF primary key (entity_status_id, entity_type_id)
);

comment on table EntityStatusesRef is
'Справочник статусов ';

/*==============================================================*/
/* Table: EntityTypeRef                                         */
/*==============================================================*/
create table EntityTypeRef (
   entity_type_id       TIdCode              not null,
   entity_title         TStr100              not null,
   constraint PK_ENTITYTYPEREF primary key (entity_type_id)
);

comment on table EntityTypeRef is
'Cправочник сущностей';

/*==============================================================*/
/* Table: Function2Role                                         */
/*==============================================================*/
create table Function2Role (
   function_id          TIdCode              not null,
   role_id              TIdCode              not null,
   assign_date          TDate                not null,
   constraint PK_FUNCTION2ROLE primary key (function_id, role_id)
);

comment on table Function2Role is
'Функции присвоенные роли';

/*==============================================================*/
/* Table: FunctionsRef                                          */
/*==============================================================*/
create table FunctionsRef (
   function_id          TIdUser              not null,
   function_code        TStr30               not null,
   function_name        TStr50               not null,
   constraint PK_FUNCTIONSREF primary key (function_id)
);

comment on table FunctionsRef is
'Функции пользователей';

/*==============================================================*/
/* Table: RegistryParams                                        */
/*==============================================================*/
create table RegistryParams (
   param_id             TIdCode              not null,
   parent_param_id      TIdCode              null,
   param_name           TStr50               not null,
   param_value          TStr100              not null,
   param_descr          TStr100              null,
   constraint PK_REGISTRYPARAMS primary key (param_id, param_name)
);

comment on table RegistryParams is
'Настроченые параметры SMT';

/*==============================================================*/
/* Table: Roles                                                 */
/*==============================================================*/
create table Roles (
   role_id              TIdUser              not null,
   role_code            TStr30               not null,
   role_name            TStr50               not null,
   constraint PK_ROLES primary key (role_id)
);

comment on table Roles is
'Роли пользователей';

/*==============================================================*/
/* Table: User2Role                                             */
/*==============================================================*/
create table User2Role (
   user_id              TIdUser              not null,
   role_id              TIdCode              not null,
   assign_date          TDate                not null,
   constraint PK_USER2ROLE primary key (user_id, role_id)
);

comment on table User2Role is
'Роли присвоенные пользователям';

/*==============================================================*/
/* Table: Users                                                 */
/*==============================================================*/
create table Users (
   user_id              TIdUser              not null,
   login                TStr20               not null,
   password             TStr100              not null,
   name                 TStr100              not null,
   phone                TStr30               null,
   mail                 TStr40               null,
   constraint PK_USERS primary key (user_id)
);

comment on table Users is
'Пользователи';

alter table Action2Role
   add constraint FK_ACTION2R_AR2R_ACTR_ACTIONSR foreign key (action_ref_id)
      references ActionsRef (action_ref_id)
      on delete restrict on update restrict;

alter table Action2Role
   add constraint FK_ACTION2R_AR2R_ROLE_ROLES foreign key (role_id)
      references Roles (role_id)
      on delete restrict on update restrict;

alter table Actions
   add constraint FK_TA_ACTREFID foreign key (actref_id)
      references ActionsRef (action_ref_id)
      on delete restrict on update restrict;

alter table Actions
   add constraint FK_ACTIONS_ENTITY_ID_ENTITIES foreign key (entity_id)
      references Entities (entity_id)
      on delete restrict on update restrict;

alter table Actions
   add constraint FK_ACTIONS_USER_ID_USERS foreign key (user_id)
      references Users (user_id)
      on delete restrict on update restrict;

alter table Application2Role
   add constraint FK_APPLICAT_A2R_APP_I_APPLICAT foreign key (app_id, app_code)
      references ApplicationsRef (app_id, app_code)
      on delete restrict on update restrict;

alter table Application2Role
   add constraint FK_APPLICAT_A2R_ROLE__ROLES foreign key (role_id)
      references Roles (role_id)
      on delete restrict on update restrict;

alter table Entities
   add constraint FK_ENTITIES_ENTITY_ST_ENTITYST foreign key (entity_status_id, entity_type_id)
      references EntityStatusesRef (entity_status_id, entity_type_id)
      on delete restrict on update restrict;

alter table Entities
   add constraint FK_ENTITIES_ENTITY_TY_ENTITYTY foreign key (entity_type_id)
      references EntityTypeRef (entity_type_id)
      on delete restrict on update restrict;

alter table EntityKindRef
   add constraint FK_ENTITYKI_ENTITY_KI_ENTITYTY foreign key (entity_type_id)
      references EntityTypeRef (entity_type_id)
      on delete restrict on update restrict;

alter table EntityStatusesRef
   add constraint FK_ENTITYST_ENTITY_TY_ENTITYTY foreign key (entity_type_id)
      references EntityTypeRef (entity_type_id)
      on delete restrict on update restrict;

alter table Function2Role
   add constraint FK_FUNCTION_F2R_FUNCT_FUNCTION foreign key (function_id)
      references FunctionsRef (function_id)
      on delete restrict on update restrict;

alter table Function2Role
   add constraint FK_FUNCTION_F2R_ROLE__ROLES foreign key (role_id)
      references Roles (role_id)
      on delete restrict on update restrict;

alter table Roles
   add constraint FK_ROLES_REFERENCE_ENTITIES foreign key (role_id)
      references Entities (entity_id)
      on delete restrict on update restrict;

alter table User2Role
   add constraint FK_USER2ROL_ROLEID_ROLES foreign key (role_id)
      references Roles (role_id)
      on delete restrict on update restrict;

alter table User2Role
   add constraint FK_USER2ROL_USER_ID_USERS foreign key (user_id)
      references Users (user_id)
      on delete restrict on update restrict;

alter table Users
   add constraint FK_USERS_USERID_ENTITIES foreign key (user_id)
      references Entities (entity_id)
      on delete restrict on update restrict;

