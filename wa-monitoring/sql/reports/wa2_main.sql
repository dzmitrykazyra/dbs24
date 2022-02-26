WITH c_actual_phone_codes AS  (
    SELECT trim(regexp_substr(str, '[^;]+', 1, LEVEL)) fc
    FROM (SELECT '1;7;1242;1246;1264;1268;1284;1340;1345;1441;1473;1649;1658;1664;1670;1671;1684;1721;1758;1767;1784;1787;1809;1829;1849;1868;1869;1876;1939;91;62;84;39;49;34;33;90;20;27;28;30;31;32;33;34;36;39;40;41;43;44;45;46;47;48;49;51;52;53;54;55;56;57;58;60;61;62;63;64;65;66;73;74;76;77;78;79;81;82;83;84;86;89;90;91;92;93;94;95;98;210;211;212;213;214;215;216;217;218;219;220;221;222;223;224;225;226;227;228;229;230;231;232;233;234;235;236;237;238;239;240;241;242;243;244;245;246;247;248;249;250;251;252;253;254;255;256;257;258;259;260;261;262;263;264;265;266;267;268;269;290;291;292;293;294;295;296;297;298;299;350;351;352;353;354;355;356;357;358;359;370;371;372;373;374;375;376;377;378;379;380;381;382;383;384;385;386;387;388;389;420;421;422;423;424;425;426;427;428;429;500;501;502;503;504;505;506;507;508;509;590;591;592;593;594;595;596;597;598;599;670;671;672;673;674;675;676;677;678;679;680;681;682;683;684;685;686;687;688;689;690;691;692;693;694;695;696;697;698;699;800;801;802;803;804;805;806;807;808;809;850;851;852;853;854;855;856;857;858;859;870;871;872;873;874;875;876;877;878;879;880;881;882;883;884;885;886;887;888;889;960;961;962;963;964;965;966;967;968;969;970;971;972;973;974;975;976;977;978;979;990;991;992;993;994;995;996;997;998;999' str FROM dual)
CONNECT BY instr(str, ';', 1, LEVEL - 1) > 0),
    c_real_phones  AS (
select us.id, phone_num, user_id from WA_TRACKER.Subscriptionphone us, c_actual_phone_codes fc
where add_time between to_date('01.11.2021', 'DD.MM.YYYY') and to_date('30.11.2021', 'DD.MM.YYYY')
--and us.key_id is not null and us.is_valid = 1
--and add_time > to_date('01.12.2021', 'DD.MM.YYYY')
-- 4 dev only
--and id>11399500
    ),
    c_rf_fmt as (select c.fc, length(c.fc) fc_length, rf.*
from c_real_phones rf,
    c_actual_phone_codes c
where SUBSTR(rf.phone_num, 1, length(c.fc)) = c.fc),
    c_passed_fc as (select max(fc_length) over (partition by id, phone_num) as max_fc_length, f.*
from c_rf_fmt f),
    c_ReportedSubs as (
select distinct fc phone_code,
    id,
    phone_num,
    user_id
from c_passed_fc
where max_fc_length = fc_length),
    c_user_2_fc as (select user_id,
    phone_code
from (select s.*,
    min(id) over (partition by (user_id)) as min_user_id
    from c_ReportedSubs s) c
where c.id = c.min_user_id),
    c_actual_payments AS (select user_id, subs_amount, pay_type from WA_TRACKER.payment where fulfil_time between to_date('01.11.2021', 'DD.MM.YYYY') and to_date('30.11.2021', 'DD.MM.YYYY')),
    c_group_payments AS (
select u2f.phone_code, ap.subs_amount, count(*) cnt
from c_user_2_fc u2f, c_actual_payments ap
where u2f.user_id = ap.user_id
group by u2f.phone_code, ap.subs_amount
    )
--select * from c_group_payments order by phone_code
select fc.fc,
       ( select cnt from c_group_payments gp
         where gp.phone_code = fc.fc and gp.subs_amount=1
       ) trials,
       ( select cnt from c_group_payments gp
         where gp.phone_code = fc.fc and gp.subs_amount=4
       ) subs4,
       ( select cnt from c_group_payments gp
         where gp.phone_code = fc.fc and gp.subs_amount=10
       ) subs10,
       ( select cnt from c_group_payments gp
         where gp.phone_code = fc.fc and not gp.subs_amount in (1,4,10)
       ) subsX
from c_actual_phone_codes fc
order by fc