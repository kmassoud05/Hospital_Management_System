@echo off
mkdir lib
cd lib
curl -L -o mysql-connector-j-8.0.33.jar https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar
echo MySQL JDBC driver downloaded successfully!
