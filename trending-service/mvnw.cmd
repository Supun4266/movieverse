@echo off
rem Small Maven wrapper for Windows Command Prompt / PowerShell: downloads Maven the first time, then runs it.
setlocal
set MAVEN_VERSION=3.9.11
if "%MAVEN_USER_HOME%"=="" set "MAVEN_USER_HOME=%USERPROFILE%\.m2"
set "DIST_DIR=%MAVEN_USER_HOME%\wrapper\dists\apache-maven-%MAVEN_VERSION%"
set "MVN_CMD=%DIST_DIR%\apache-maven-%MAVEN_VERSION%\bin\mvn.cmd"

if exist "%MVN_CMD%" goto run
echo Downloading Maven %MAVEN_VERSION% - only the first time...
powershell -NoProfile -Command "$ErrorActionPreference='Stop'; New-Item -ItemType Directory -Force -Path '%DIST_DIR%' | Out-Null; Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/%MAVEN_VERSION%/apache-maven-%MAVEN_VERSION%-bin.zip' -OutFile '%DIST_DIR%\maven.zip'; Expand-Archive -Force -Path '%DIST_DIR%\maven.zip' -DestinationPath '%DIST_DIR%'; Remove-Item '%DIST_DIR%\maven.zip'"
if errorlevel 1 exit /b 1

:run
cd /d "%~dp0"
call "%MVN_CMD%" %*
