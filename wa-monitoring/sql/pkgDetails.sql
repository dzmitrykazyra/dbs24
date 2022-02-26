drop table wa_package_details;

/*==============================================================*/
/* Table: wa_package_details                                    */
/*==============================================================*/
create table wa_package_details (
   package_name         TStr100              not null,
   actual_date          TDateTime            not null,
   company_name         TStr100              not null,
   app_name             TStr100              not null,
   contact_info         TStr100              not null,
   constraint PK_WA_PACKAGE_DETAILS primary key (package_name)
);

comment on table wa_package_details is
'PackageDetails';
