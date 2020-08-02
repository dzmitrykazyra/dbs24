@echo off
echo Setting JAVA_HOME
set JAVA_HOME=D:\Java\jdk-14
echo setting PATH
set PATH=D:\Java\jdk-14\bin;%PATH%
echo Display java version
java -version

set logfile=compile_dev_fs24_j14.log
set devdir=d:\fs24\bank24
set devdircmd=d:\fs24\bank24\cmd

del %devdircmd%\%logfile% >nul 
cd %devdir%
cmd.exe /c mvn -T 100 -q clean compile -Dmaven.test.skip=true > %devdircmd%\%logfile%

