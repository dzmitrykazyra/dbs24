@echo off
set logfile=test_dev_wmonitor.log
set devdir=d:\fs24\dev\webmonitor-core
set devdircmd=d:\fs24\dev\cmd\tests

del %devdircmd%\%logfile% >nul 
cd %devdir%
cmd.exe /c cmd.exe /c mvn -T 10 test -Dlog4j.configuration="D:\fs24\log4j.properties" -Dmaven.test.skip=false > %devdircmd%\%logfile%
rem mvn test >nul 
