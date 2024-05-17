package com.supos.app.domain.vo;

import java.util.List;

public class WarehousestoragelocationIdmap {
    private Long id;
    private String  name;
    List<WarehouseNamemap> warehouseNamemap;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public List<WarehouseNamemap> getWarehouseNamemap() {
        return warehouseNamemap;
    }

    public void setWarehouseNamemap(List<WarehouseNamemap> warehouseNamemap) {
        this.warehouseNamemap = warehouseNamemap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


