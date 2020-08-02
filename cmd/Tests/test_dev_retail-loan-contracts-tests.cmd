@echo off
del d:\fs24\dev\cmd\test_retail-loan-contracts-tests_*.log >nul 

cd d:\fs24\dev\retail-loan-contracts-tests
cmd.exe /c cmd.exe /c mvn test -Dlog4j.configuration="D:\fs24\log4j.properties" -Dmaven.test.skip=false  > d:\fs24\dev\cmd\test_retail-loan-contracts-tests_main1.log
rem cmd.exe /c cmd.exe /c mvn test -Dlog4j.configuration="D:\fs24\log4j.properties" -Dmaven.test.skip=false  > d:\fs24\dev\cmd\test_retail-loan-contracts-tests_main2.log
rem cmd.exe /c cmd.exe /c mvn test -Dlog4j.configuration="D:\fs24\log4j.properties" -Dmaven.test.skip=false  > d:\fs24\dev\cmd\test_retail-loan-contracts-tests_main3.log
rem pause

