@echo off
cd /d "%~dp0"
echo Iniciando Essencia da Fruta...
echo.
where mvn >nul 2>nul
if errorlevel 1 (
    echo Maven nao encontrado. Instale o Maven e o JDK 21 e tente novamente.
    pause
    exit /b 1
)
mvn -q compile exec:java -Dexec.mainClass="com.essenciadafruta.App"
pause
