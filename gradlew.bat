@echo off
set DIR=%~dp0
set GRADLE_USER_HOME=%DIR%.gradle
"%DIR%\jdk\bin\java.exe" -jar "%DIR%\gradle\wrapper\gradle-wrapper.jar" %*
