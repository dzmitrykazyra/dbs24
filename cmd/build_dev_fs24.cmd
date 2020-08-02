@echo off

set logfile=build_dev_fs24.log
set devdir=d:\fs24\BANK24
set devdircmd=d:\fs24\BANK24\cmd

del %devdircmd%\%logfile% >nul 
cd %devdir%
cmd.exe /c mvn -q -T 100 clean install -Dmaven.test.skip=true > %devdircmd%\%logfile%

