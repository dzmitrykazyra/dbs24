@echo off
cls
setlocal

@echo off
echo Setting JAVA_HOME
set JAVA_HOME=D:\Java\jdk-14
echo setting PATH
set PATH=D:\Java\jdk-14\bin;%PATH%
echo Display java version
java -version


set logfile=build_dev_retail_loan_contracts.log
set devdir=d:\fs24\dev_new
set devdircmd=d:\fs24\dev_new\cmd

del %devdircmd%\%logfile% >nul 

rem ������������� WF
rem cd d:\Java\WF17m\bin\
rem call jboss-cli.bat --connect command=:shutdown --controller=localhost:9992

rem ������ ���� � ������ ������
cd d:\fs24
call drop_fs24.cmd

SET prj_list=entity-core-cc,entity-contract-cc,references-application-cc,retail-loan-contracts,retail-loan-contracts-tests,counterparties-cc,security-manager-api,jdbc-api,retail-loan-contracts-services,persistence-api,entity-core-api,entity-core,retail-loan-contracts-tariffs,tariff-core
echo prj_list = %prj_list%

rem ����������� jar
cd %devdir%
cmd.exe /c mvn -T 4 -q -pl %prj_list% clean install -Dfile.encoding=UTF-8 -Dmaven.test.skip=true > %devdircmd%\%logfile%

rem cd d:\Java\WF17m\bin\
rem cmd.exe /c standalone.conf.bat
endlocal
