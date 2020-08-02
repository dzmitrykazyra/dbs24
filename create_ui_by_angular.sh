# @echo off
module_name=$1

echo $module_name

if [ -z $module_name ]
then

  echo "module name is missing!"

else

  new_module_name=$1'-ui'
  echo "create "$new_module_name


  #rem ng new module_name%
  ng new $new_module_name --routing=true --style=css

fi