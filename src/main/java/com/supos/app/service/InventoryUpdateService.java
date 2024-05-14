package com.supos.app.service;

import com.supos.app.entity.WmsTask;

public interface InventoryUpdateService {
    void updateMaterialStorageLocation(Long material_id, Long location_id, int quantity, boolean isInbound);

    void updatePeopleAndResourceByRule(WmsTask wmsTask);
}
