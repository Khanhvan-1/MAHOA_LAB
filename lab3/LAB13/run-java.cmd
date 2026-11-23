@echo off
setlocal
cd /d "%~dp0LAB3_MaHoaKhoaCongKhai"
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.21.9-hotspot"
set "PATH=%JAVA_HOME%\bin;%APPDATA%\Code\User\globalStorage\pleiades.java-extension-pack-jdk\maven\latest\bin;%PATH%"
call mvn -B -ntp package
if errorlevel 1 (
  pause
  exit /b 1
)
java -Dfile.encoding=UTF-8 -cp "target/classes;target/dependency/*" week_03.frm_Menu
if errorlevel 1 pause
