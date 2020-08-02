@echo off
rem cd "d:\Java\wfl14\standalone\deployments\"
rem call drop_all.cmd

cd d:\fs24\dev\webmonitor
cmd.exe /c cmd.exe /c mvn deploy  > d:\fs24\dev\cmd\webmonitor_deploy.log

