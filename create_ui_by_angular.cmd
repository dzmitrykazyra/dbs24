echo off
set module_name=%1

echo %module_name%

IF module_name=="/" (

echo "module name is missing!"

pause

) ELSE (

rem ng new %module_name%
ng new %module_name%-ui --routing=true --style=css

)