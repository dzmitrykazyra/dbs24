@echo off

set logfile=build_dev_dbs24.log
set devdir=d:\dbs24
set devdircmd=d:\dbs24\cmd

del %devdircmd%\%logfile% >nul 
cd %devdir%
cmd.exe /c mvn -q -T 100 clean install -Dmaven.test.skip=true > %devdircmd%\%logfile%

