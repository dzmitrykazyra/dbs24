-- Application2Role

-- ������� ����������
ALTER TABLE Application2Role DROP CONSTRAINT FK_APPLICAT_A2R_APP_I_APPLICAT;

-- ������� ��������� ����
ALTER TABLE Application2Role DROP CONSTRAINT PK_APPLICATION2ROLE;

-- ������� �������
ALTER TABLE Application2Role DROP COLUMN app_code;

-- ��������������� ����������
ALTER TABLE Application2Role ADD constraint PK_APPLICATION2ROLE primary key (app_id, role_id)


-- ApplicationsRef

-- ������� ��������� ����
ALTER TABLE ApplicationsRef DROP CONSTRAINT PK_APPLICATIONSREF;

-- �������� ��������� ����
ALTER TABLE ApplicationsRef ADD constraint PK_APPLICATIONSREF primary key (app_id);


-- ��������������� ���������� � ������� Application2Role
alter table Application2Role
   add constraint FK_APPLICAT_A2R_APP_I_APPLICAT foreign key (app_id)
      references ApplicationsRef (app_id)
      on delete restrict on update restrict;


-- AppFieldsCaptions

create table AppFieldsCaptions (
   user_id              TIdUser              not null,
   app_id               TIdCode              not null,
   field_name           TStr100              not null,
   field_caption        TStr100              not null,
   field_tooltip       	TStr100              null,
   constraint PK_APPFIELDSCAPTIONS primary key (user_id, app_id, field_name)
);

comment on table AppFieldsCaptions is
'��������� ������������ ����� ���������
';

alter table AppFieldsCaptions
   add constraint FK_AFC_APP_ID foreign key (app_id)
      references ApplicationsRef (app_id)
      on delete restrict on update restrict;

alter table AppFieldsCaptions
   add constraint FK_AFC_USER_ID foreign key (user_id)
      references Users (user_id)
      on delete restrict on update restrict;