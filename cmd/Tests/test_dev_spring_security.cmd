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


set logfile_build=build_dev_spring-security-test.log
set logfile=test_dev_spring-security-test.log
set rootdevdir=d:\fs24\dev-s
set devdir=d:\fs24\dev-s\spring-security
set devdircmd=d:\fs24\dev-s\cmd\tests

del %devdircmd%\%logfile_build% >nul 
del %devdircmd%\%logfile% >nul 


rem чистим логи и старые версии
cd d:\fs24
call drop_fs24.cmd

cd d:\fs24\DEV\log4j\
call drop_logs.cmd

SET prj_list=persistence-api,spring-security-api,spring-boot-api,spring-core-api,entity-core,spring-config,spring-junit
echo prj_list = %prj_list%

rem компилируем jar
cd %rootdevdir%

cmd.exe /c mvn -T 4 -q -pl %prj_list% clean install -Dfile.encoding=UTF-8 -Dmaven.test.skip=true > %devdircmd%\%logfile_build% 
rem cmd.exe /c mvn -T 50 -q clean install -Dfile.encoding=UTF-8 -Dmaven.test.skip=true > %devdircmd%\%logfile_build%

rem тестирование 
cd %devdir%
cmd.exe /c mvn install test -Dfile.encoding=UTF-8 -DskipTests=false > %devdircmd%\%logfile%

rem cd d:\Java\WF17m\bin\
rem cmd.exe /c standalone.conf.bat
endlocal
