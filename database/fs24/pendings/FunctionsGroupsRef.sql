
--drop table FunctionsGroupsRef CASCADE;
/*==============================================================*/
/* Table: FunctionsGroupsRef                                    */
/*==============================================================*/
create table FunctionsGroupsRef (
   function_group_id    TIdUser              not null,
   function_group_code  TStr30               not null,
   function_group_name  TStr100              not null,
   constraint PK_FUNCTIONSGROUPSREF primary key (function_group_id)
);

comment on table FunctionsGroupsRef is
'Группы функций системы';


drop table FunctionsRef CASCADE;

/*==============================================================*/
/* Table: FunctionsRef                                          */
/*==============================================================*/
create table FunctionsRef (
   function_id          TIdUser              not null,
   function_group_id    TIdCode              not null,
   function_code        TStr30               not null,
   function_name        TStr100              not null,
   constraint PK_FUNCTIONSREF primary key (function_id)
);

comment on table FunctionsRef is
'Функции пользователей';

alter table FunctionsRef
   add constraint FK_FGR_function_group_id foreign key (function_group_id)
      references FunctionsGroupsRef (function_group_id)
      on delete restrict on update restrict;



alter table Function2Role
   add constraint FK_FUNCTION_F2R_FUNCT_FUNCTION foreign key (function_id)
      references FunctionsRef (function_id)
      on delete restrict on update restrict;


alter table FunctionsRef
   add constraint FK_FGR_function_group_id foreign key (function_group_id)
      references FunctionsGroupsRef (function_group_id)
      on delete restrict on update restrict;


