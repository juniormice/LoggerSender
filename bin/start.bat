@echo off
SETLOCAL ENABLEDELAYEDEXPANSION
@set basedir="%~dp0"
pushd %basedir%


@set libpath=%cd%\..\lib\*
@set confpath=%cd%\..\config
@set classpath=!libpath!
@set basedir=%cd%\..\
rem set java env
popd
java -cp !classpath! -Dconfig.file.path=!confpath!\conf.json -Dlogs.dir=!basedir! com.dbapp.Main

ENDLOCAL