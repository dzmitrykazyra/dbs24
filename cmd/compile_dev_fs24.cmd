@echo off

set logfile=compile_dev_fs24.log
set devdir=d:\fs24\BANK24
set devdircmd=d:\fs24\BANK24\cmd

del %devdircmd%\%logfile% >nul 
cd %devdir%
cmd.exe /c mvn -T 100 -q compile -Dmaven.test.skip=true > %devdircmd%\%logfile%


