$ErrorActionPreference = 'Stop'
Set-Location (Join-Path $PSScriptRoot 'LAB2_MaHoaHienDai')
if (-not $env:JAVA_HOME) {
    $labJdk = Get-ChildItem 'C:\Program Files\Eclipse Adoptium' -Directory -ErrorAction SilentlyContinue | Where-Object Name -Like 'jdk-17*' | Select-Object -First 1
    if ($labJdk) { $env:JAVA_HOME = $labJdk.FullName }
}
$labMaven = Get-Command mvn.cmd -ErrorAction SilentlyContinue
if ($labMaven) { $labMavenPath = $labMaven.Source } else {
    $labMavenPath = Join-Path $env:APPDATA 'Code\User\globalStorage\pleiades.java-extension-pack-jdk\maven\latest\bin\mvn.cmd'
}
if (-not (Test-Path -LiteralPath $labMavenPath)) { throw 'Khong tim thay Maven. Cai Maven hoac them mvn vao PATH.' }
& $labMavenPath -B -ntp package
if ($LASTEXITCODE -ne 0) { throw 'Build that bai. Xem cac dong ERROR o tren.' }
Write-Host 'BUILD OK. Chay run-java.cmd de mo menu.'
