set JAVA_HOME=C:\openjdk-21.0.1_windows-x64

set BD_URL="jdbc:sqlite:C:\BASIS\EBSERH\testeTecnicoEBSERH\pacientesEBSERH.sqlite"
set BD_USUARIO=
set BD_SENHA=
set BD_SIMULTANEO=3

C:\openjdk-21.0.1_windows-x64\bin\java.exe -Dquarkus.http.host=0.0.0.0 -Dquarkus.http.port=8080 -Dquarkus.native.native-image-xmx=6g -jar .\target\quarkus-app\quarkus-run.jar