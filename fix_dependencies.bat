@echo off
cd %~dp0

echo Cleaning up old SLF4J files...
del "lib\slf4j-api-1.7.36 (1).jar" 2>nul
del "lib\slf4j-api-1.7.36.jar" 2>nul
del "lib\slf4j-simple-1.7.36.jar" 2>nul

echo Downloading new SLF4J files...
curl -L "https://repo1.maven.org/maven2/org/slf4j/slf4j-api/1.7.36/slf4j-api-1.7.36.jar" --output "lib\slf4j-api-1.7.36.jar"
curl -L "https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/1.7.36/slf4j-simple-1.7.36.jar" --output "lib\slf4j-simple-1.7.36.jar"

echo Done! Please rebuild your project in NetBeans.
pause
