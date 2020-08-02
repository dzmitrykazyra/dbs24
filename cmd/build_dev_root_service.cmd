@echo off
cls
setlocal

set logfile=build_dev_root-service.log
set devdir=d:\fs24\dev
set devdircmd=d:\fs24\dev\cmd

del %devdircmd%\%logfile% >nul 

rem останавливаем WF
cd d:\Java\wildfly-17.0.1.Final\bin\
call jboss-cli.bat --connect command=:shutdown --controller=localhost:9981

rem чистим логи и старые версии
cd d:\fs24
call drop_vcs.cmd

SET prj_list=rest-api,root-service
echo prj_list = %prj_list%

rem компилируем jar
cd %devdir%
cmd.exe /c mvn -T 4 -q -pl %prj_list% clean install -Dfile.encoding=UTF-8 -Dmaven.test.skip=true > %devdircmd%\%logfile%

rem cd d:\Java\wildfly-17.0.1.Final\bin\
rem cmd.exe /c standalone.conf.bat
endlocal
