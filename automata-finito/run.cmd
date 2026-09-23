@echo off
setlocal
cd /d "%~dp0"

set "JAR_PATH=target\automata-finito-1.0.0-SNAPSHOT.jar"

if not exist "%JAR_PATH%" (
    echo No existe el JAR. Compilando el proyecto...
    call mvnw.cmd clean package
    if errorlevel 1 exit /b 1
)

java -jar "%JAR_PATH%"
