@echo off
set JAVA_HOME=C:\openjdk-21.0.1_windows-x64

rem --- Configuração Banco de Dados ---
rem set BD_URL=jdbc:sqlite:C:\BASIS\EBSERH\testeTecnicoEBSERH\pacientesEBSERH.sqlite
rem set BD_USUARIO=
rem set BD_SENHA=
rem set BD_SIMULTANEO=3

rem --- Keycloak / OIDC: substitua pelos valores reais do seu realm ---
rem set OIDC_AUTH_SERVER_URL=https://host-keycloak/auth/realms/ebserh
rem set OIDC_CLIENT_ID=pacientes-api
rem set OIDC_SECRET=aaaaabbbbcccc
rem set OIDC_SWAGGER_CLIENT_ID=pacientes-swagger

rem mvn clean install

%JAVA_HOME%\bin\java -jar .\target\pacientes-0.0.1-SNAPSHOT-runner.jar