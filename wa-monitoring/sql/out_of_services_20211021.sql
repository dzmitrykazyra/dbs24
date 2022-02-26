--drop table core_out_of_service;

/*==============================================================*/
/* Table: core_out_of_service                                   */
/*==============================================================*/
create table core_out_of_service (
   create_date          TDateTime            not null,
   actual_date          TDateTime            null,
   service_date_start   TDateTime            null,
   service_date_finish  TDateTime            null,
   note                 TStr10000            null,
   constraint PK_CORE_OUT_OF_SERVICE primary key (create_date)
);

comment on table core_out_of_service is
'Сервисные периоды';


--drop table core_out_of_service_hist;

/*==============================================================*/
/* Table: core_out_of_service_hist                              */
/*==============================================================*/
create table core_out_of_service_hist (
   actual_date          TDateTime            null,
   service_date_start   TDateTime            null,
   service_date_finish  TDateTime            null,
   note                 TStr10000            null
);

comment on table core_out_of_service_hist is
'Сервисные периоды - hist';
