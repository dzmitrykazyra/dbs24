--select * from PaymentSystemDocTemplatesRef
delete from PaymentSystemDocTemplatesRef;
--select * from DocTemplateAttrsRef
delete from DocTemplateAttrsRef;
--select * from DocAttrsRef
delete from DocAttrsRef;
--select * from DocTemplatesRef;
delete from DocTemplatesRef;
--select * from paymentSystemsRef
delete from paymentSystemsRef;
--select * from DocTypesRef
delete from DocTypesRef;
--select * from DocTemplateGroupsRef
delete from DocTemplateGroupsRef;
delete from fs24_dev.referencesversions;
SELECT * FROM fs24_dev.referencesversions LIMIT 200;
