@echo off
setlocal
cd /d "%~dp0LAB2_Python"
if not exist ".venv\Scripts\python.exe" (
    python -m venv .venv
    if errorlevel 1 goto failed
)
".venv\Scripts\python.exe" -c "import cryptography" >nul 2>&1
if errorlevel 1 (
    ".venv\Scripts\python.exe" -m pip install -r requirements.txt
    if errorlevel 1 goto failed
)
".venv\Scripts\python.exe" aes_cbc_gui.py %*
if errorlevel 1 goto failed
exit /b 0
:failed
echo Khong chay duoc Python. Kiem tra Python va ket noi Internet.
pause
exit /b 1
