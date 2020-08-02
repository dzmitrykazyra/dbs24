@echo off
del d:\fs24\dev\cmd\build_dev_and_test_fs24.log >nul 
cd d:\fs24\dev
cmd.exe /c mvn clean install -Dlog4j.configuration="D:\fs24\log4j.properties" -Dmaven.test.skip=false > d:\fs24\dev\cmd\build_dev_and_test_fs24.log
rem mvn clean install >nul 
