@echo off
set JAVA_HOME=C:\openjdk-21.0.1_windows-x64

rem set BD_URL=jdbc:sqlite:C:\BASIS\EBSERH\testeTecnicoEBSERH\pacientesEBSERH.sqlite
rem set BD_USUARIO=
rem set BD_SENHA=
rem set BD_SIMULTANEO=3

rem mvn clean install

%JAVA_HOME%\bin\java -jar .\target\pacientes-0.0.1-SNAPSHOT-runner.jar