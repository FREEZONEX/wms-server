package com.supos.app.service;

import com.supos.app.entity.WmsInventoryOperation;
import com.supos.app.entity.WmsInventoryOperationDetail;
import com.supos.app.entity.WmsTask;

public interface InventoryUpdateService {

    void updateStorageLocationMaterial(Long location_id, Long material_id, int quantity, boolean isInbound);

    void updateMaterialStorageLocation(Long material_id, Long location_id, int quantity, boolean isInbound);

    void updatePeopleAndResourceByRule(WmsTask wmsTask);
}
