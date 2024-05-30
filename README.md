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
# Generate Dummy Data % Import To DB
Run src/main/resources/sql/data_generator.py to generate storage location with random material stocked data
```
% python3 data_generator.py
SQL file has been generated.
```
Import Mysql Data:
```
mysql -h 121.7.36.93 -P 32307 -u root -p
Enter password: 
mysql> use wms
Database changed
mysql> source data.sql
```

# Export Data From DB
execute below command in src/main/resources/sql to export all data to data.sql
```commandline
mysqldump -h 127.0.0.1 -P 6033 -u root -p wms \
wms_warehouse \
wms_storage_location \
wms_material \
wms_material_expected_location \
wms_rfid_material \
wms_material_storage_location \
wms_inbound \
wms_inbound_detail \
wms_outbound \
wms_outbound_detail \
wms_stocktaking \
wms_stocktaking_detail \
wms_resource \
wms_resource_occupy_log \
wms_task \
wms_task_resource \
wms_rule \
wms_prediction \
--no-create-info --skip-triggers --complete-insert --skip-extended-insert --skip-comments --compact > data.sql
```