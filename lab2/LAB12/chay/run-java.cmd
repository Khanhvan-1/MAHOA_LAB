@echo off
setlocal
cd /d "%~dp0LAB2_MaHoaHienDai"
if not exist "target\classes\week_02\frm_Menu.class" goto build
if not exist "target\dependency\mssql-jdbc-12.8.1.jre11.jar" goto build
if not exist "target\dependency\bcprov-jdk18on-1.78.1.jar" goto build
goto run
:build
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0build-java.ps1"
if errorlevel 1 goto failed
:run
java -Dfile.encoding=UTF-8 -cp "target/classes;target/dependency/*" week_02.frm_Menu
if errorlevel 1 goto failed
exit /b 0
:failed
echo Khong chay duoc. Kiem tra java -version va build-java.ps1.
pause
exit /b 1
