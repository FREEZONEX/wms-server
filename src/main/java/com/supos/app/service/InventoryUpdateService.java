package com.supos.app.service;

import com.supos.app.domain.entity.WmsStorageLocation;
import com.supos.app.domain.entity.WmsTask;

import java.util.List;

public interface InventoryUpdateService {

    WmsStorageLocation updateStorageLocationMaterial(Long location_id, Long material_id, int quantity, boolean isInbound);

    void updateMaterialStorageLocation(Long material_id, Long location_id, int quantity, boolean isInbound);

    List<String> updatePeopleAndResourceByRule(WmsTask wmsTask);
}
