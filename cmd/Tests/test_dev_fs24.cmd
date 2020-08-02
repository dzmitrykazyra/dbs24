@echo off
set logfile=test_dev_fs24.log
set devdir=d:\fs24\BANK24
set devdircmd=d:\fs24\BANK24\cmd\tests

del %devdircmd%\%logfile% >nul 
cd %devdir%
cmd.exe /c cmd.exe /c mvn -T 100 -q test  -Dmaven.test.skip=false > %devdircmd%\%logfile%
rem mvn test >nul 
