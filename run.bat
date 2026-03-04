@echo off
cd /d "%~dp0"
echo Iniciando Essencia da Fruta...
echo.
mvn exec:java -Dexec.mainClass="com.essenciadafruta.App"
pause
