$ErrorActionPreference = 'Stop'
& (Join-Path $PSScriptRoot 'build-java.ps1')
Set-Location (Join-Path $PSScriptRoot 'LAB2_MaHoaHienDai')
& "$env:JAVA_HOME\bin\java.exe" -Dfile.encoding=UTF-8 -cp 'target/test-classes;target/classes;target/dependency/*' week_02.Lab2SelfTest
if ($LASTEXITCODE -ne 0) { throw 'Self-test that bai.' }
