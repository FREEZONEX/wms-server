# WMS Backend
Developed by SpringBoot framework and MySQL
# Build Docker Image
Use "make" command to build docker image
# Generate Swagger API Document
- Run app and visit "http://localhost:8085/swagger-ui.html" to auto generate help document
- Download Swagger JSON from http://localhost:8085/v2/api-docs
- Use Redoc CLI to generate single html page
```
npm install -g redoc-cli
redoc-cli bundle -o redoc-static.html api-docs.json
```
# Dummy Data Generate % Import
Run src/main/resources/sql/data_generator.py to generate storage location with random material stocked data
```
% python3 data_generator.py
SQL file has been generated.
```
Import Mysql Data:
```
mysql -h 47.236.10.165 -P 32307 -u root -p
Enter password: 
mysql> use wms
Database changed
mysql> source wms_storage_location.sql
```