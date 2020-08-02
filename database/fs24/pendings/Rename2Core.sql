ALTER TABLE EntityTypesRef
  RENAME TO core_EntityTypesRef;

ALTER TABLE EntityKindsRef
  RENAME TO core_EntityKindsRef;

ALTER TABLE EntityStatusesRef
  RENAME TO core_EntityStatusesRef;

ALTER TABLE ActionCodesRef
  RENAME TO core_ActionCodesRef;

ALTER TABLE EntityMarks
  RENAME TO core_EntityMarks;

ALTER TABLE Entities
  RENAME TO core_Entities;

ALTER TABLE Actions
  RENAME TO core_Actions;

ALTER TABLE EntityTests
  RENAME TO core_EntityTests;

ALTER TABLE Action2Role
  RENAME TO core_Action2Role;

ALTER TABLE ContractSubjectsRef
  RENAME TO core_ContractSubjectsRef;

ALTER TABLE EntityContracts
  RENAME TO core_EntityContracts;

ALTER TABLE Users
  RENAME TO core_Users;

ALTER TABLE User2Role
  RENAME TO core_User2Role;

ALTER TABLE Roles
  RENAME TO core_Roles;

ALTER TABLE Application2Role
  RENAME TO core_Application2Role;

ALTER TABLE ApplicationsRef
  RENAME TO core_ApplicationsRef;

ALTER TABLE Counterparties
  RENAME TO core_Counterparties;

ALTER TABLE RegistryParams
  RENAME TO core_RegistryParams;

ALTER TABLE Function2Role
  RENAME TO core_Function2Role;

ALTER TABLE AppFieldsCaptions
  RENAME TO core_AppFieldsCaptions;

ALTER TABLE MarksRef
  RENAME TO core_MarksRef;

ALTER TABLE MarksValuesRef
  RENAME TO core_MarksValuesRef;

ALTER TABLE Currency2Group
  RENAME TO core_Currency2Group;

ALTER TABLE CurrenciesRef
  RENAME TO core_CurrenciesRef;

ALTER TABLE CurrencyRates
  RENAME TO core_CurrencyRates;

ALTER TABLE CurrenciesRatesTypesRef
  RENAME TO core_CurrenciesRatesTypesRef;

ALTER TABLE CurrenciesGroupsRef
  RENAME TO core_CurrenciesGroupsRef;

ALTER TABLE FunctionsRef
  RENAME TO core_FunctionsRef;

ALTER TABLE FunctionsGroupsRef
  RENAME TO core_FunctionsGroupsRef;

//========================================


ALTER TABLE core_PmtScheduleLinesf
  RENAME TO core_PmtScheduleLines;

ALTER TABLE PmtSchedules
  RENAME TO core_PmtSchedules;

ALTER TABLE PmtScheduleAlgsRef
  RENAME TO core_PmtScheduleAlgsRef;

ALTER TABLE PmtScheduleTermsRef
  RENAME TO core_PmtScheduleTermsRef;

