package com.supos.app.vo;

import com.supos.app.entity.WmsStorageLocation;

import java.util.Date;
import java.util.List;

public class StorageLocationSelectAllResponse {
    public StorageLocationSelectAllResponse(WmsStorageLocation wmsStorageLocation) {
        this.id = wmsStorageLocation.getId();
        this.warehouse_id = wmsStorageLocation.getWarehouse_id();
        this.name = wmsStorageLocation.getName();
        this.material_name = wmsStorageLocation.getMaterial_name();
        this.quantity = wmsStorageLocation.getQuantity();
        this.update_time = wmsStorageLocation.getUpdate_time();
        this.create_time = wmsStorageLocation.getCreate_time();
    }

    private Long id;
    private Long warehouse_id;
    private String name;
    private String material_name;;
    private Integer quantity;
    private Date update_time;
    private Date create_time;

    private static final long serialVersionUID = 1L;

    List<StorageLocationSelectAllMaterial> materials;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWarehouse_id() {
        return warehouse_id;
    }

    public void setWarehouse_id(Long warehouse_id) {
        this.warehouse_id = warehouse_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaterial_name() {
        return material_name;
    }

    public void setMaterial_name(String material_name) {
        this.material_name = material_name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public List<StorageLocationSelectAllMaterial> getMaterials() {
        return materials;
    }

    public void setMaterials(List<StorageLocationSelectAllMaterial> materials) {
        this.materials = materials;
    }

}
