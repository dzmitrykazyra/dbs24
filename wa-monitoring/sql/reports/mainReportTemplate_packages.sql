with c_real_phones as (select sp.subscription_id,
                              sp.phone_num,
                              sp.contract_id,
                              sp.user_id,
                              uc.contract_type_id,
                              uc.begin_date,
                              uc.end_date,
                              uc.contract_status_id
                       from wa_users_subscriptions sp,
                            wa_users_contracts uc
                       where (sp.subscription_status_id in (0, 1, 2))
                         and sp.contract_id = uc.contract_id
                         and uc.contract_type_id >= 10
                         and uc.end_date >= '2021.12.01'),
     c_rf_fmt as (select c.fc, length(c.fc) fc_length, rf.*
                  from c_real_phones rf,
                       v_ActualPhoneCodes c
                  where SUBSTR(rf.phone_num, 1, length(c.fc)) = c.fc),
     c_passed_fc as (select max(fc_length) over (partition by (subscription_id, phone_num)) as max_fc_length, f.*
                     from c_rf_fmt f),
     c_ReportedSubs as (
         select fc phone_code,
                subscription_id,
                phone_num,
                contract_id,
                user_id,
                contract_type_id,
                contract_status_id,
                begin_date,
                end_date
         from c_passed_fc
         where max_fc_length = fc_length),
     c_ActualReportedSubs as (select phone_code,
                                     contract_id,
                                     user_id,
                                     contract_type_id,
                                     contract_status_id,
                                     begin_date,
                                     end_date
                              from (select s.*,
                                           min(subscription_id) over (partition by (contract_id)) as min_subscription_id
                                    from c_ReportedSubs s) c
                              where c.subscription_id = c.min_subscription_id),
     c_ContractsByPhoneCodeAndDeviceTypeId as (
         select phone_code,
                user_id,
                --device_type_id,
                app_name,
                contract_id,
                contract_type_id,
                contract_status_id,
                begin_date,
                end_date
         from (
                  select v.*,
                         ud.device_type_id,
                         ud.app_name,
                         max(device_type_id) over (partition by (contract_id)) as max_device_type_id
                  from c_ActualReportedSubs v,
                       wa_users_devices ud
                  where v.user_id = ud.user_id
              ) c
         where device_type_id = max_device_type_id),
     c_ActualContractsByPhoneCodeAndDeviceTypeId as (select ac.*,
                                                            (select count(*)
                                                             from wa_users_contracts uc
                                                             where uc.user_id = ac.user_id
                                                               and uc.contract_id < ac.contract_id
                                                               and uc.begin_date < '2021.12.01') prev_contracts_amt,
                                                            (select count(*)
                                                             from wa_users_contracts uc
                                                             where uc.user_id = ac.user_id
                                                               and uc.contract_id < ac.contract_id
                                                               and uc.contract_type_id > 10
                                                               and uc.begin_date < '2021.12.01') prev_pmt_contracts_amt,
                                                            (select count(*)
                                                             from wa_users_contracts uc
where uc.user_id = ac.user_id
                                                               and uc.contract_type_id > 10
                                                               and uc.contract_status_id <> 0
                                                               and uc.begin_date < '2021.12.01') closed_contracts_amt
                                                     from c_ContractsByPhoneCodeAndDeviceTypeId ac),
     c_DeviceByPhoneCode as (select distinct phone_code, app_name
                             from c_ContractsByPhoneCodeAndDeviceTypeId)
select app_name,
       dfc.phone_code,
       (select count(*)
        from c_ActualContractsByPhoneCodeAndDeviceTypeId asb
        where asb.app_name = dfc.app_name
          and asb.phone_code = dfc.phone_code
          and asb.contract_status_id = 0
          and asb.contract_type_id = 20)                               active_subs_basic,
       (select count(*)
        from c_ActualContractsByPhoneCodeAndDeviceTypeId asb
        where asb.app_name = dfc.app_name
          and asb.phone_code = dfc.phone_code
          and asb.contract_status_id = 0
          and asb.contract_type_id = 30)                               active_subs_std,
       (select count(*)
        from c_ActualContractsByPhoneCodeAndDeviceTypeId asb
        where asb.app_name = dfc.app_name
          and asb.phone_code = dfc.phone_code
          and asb.contract_status_id = 0
          and asb.contract_type_id = 40)                               active_subs_premium,
       (select count(*)
        from c_ActualContractsByPhoneCodeAndDeviceTypeId asb
        where asb.app_name = dfc.app_name
          and asb.phone_code = dfc.phone_code
          and asb.contract_type_id = 10)                               active_subs_trial,
       (select count(*)
        from c_ActualContractsByPhoneCodeAndDeviceTypeId asb
        where asb.app_name = dfc.app_name
          and asb.phone_code = dfc.phone_code
          and asb.end_date > '2021.12.01'
          and asb.prev_contracts_amt = 0)                         new_users,
       (select count(*)
        from c_ActualContractsByPhoneCodeAndDeviceTypeId asb
        where asb.app_name = dfc.app_name
          and asb.phone_code = dfc.phone_code
          and asb.contract_status_id = 0
          and asb.begin_date >= '2021.12.01'
          and asb.contract_type_id > 10)                               new_pmt_users,
       (select count(*)
        from c_ActualContractsByPhoneCodeAndDeviceTypeId asb
        where asb.app_name = dfc.app_name
          and asb.phone_code = dfc.phone_code
          and asb.contract_status_id = 0
          and asb.prev_pmt_contracts_amt > 0)                          old_users_new_subs,
       (select count(*)
        from c_ActualContractsByPhoneCodeAndDeviceTypeId asb
        where asb.app_name = dfc.app_name
          and asb.phone_code = dfc.phone_code
          and asb.contract_status_id = 0
          and asb.begin_date <= '2021.12.01')                          old_users_recuring_subs,
       (select count(*)
        from c_ActualContractsByPhoneCodeAndDeviceTypeId asb
        where asb.app_name = dfc.app_name
          and asb.phone_code = dfc.phone_code
          and asb.contract_status_id = 1
          and asb.closed_contracts_amt > 0)                            expiried_subs
from c_DeviceByPhoneCode dfc
order by dfc.app_name desc, dfc.phone_code asc