delete from wms_inbound;
delete from wms_outbound;
delete from wms_task;
delete from wms_material_storage_location;
update wms_storage_location set material_name=NULL,quantity=NULL