package com.supos.app.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supos.app.entity.*;
import com.supos.app.mapper.*;
import com.supos.app.service.WmsInboundService;
import com.supos.app.service.InventoryUpdateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class WmsInboundServiceImpl extends ServiceImpl<WmsInboundMapper, WmsInventoryOperation>
        implements WmsInboundService {

    @Autowired
    WmsRfidMaterialServiceImpl wmsRfidMaterialServiceImpl;
    @Autowired
    private WmsInboundMapper wmsInventoryOperationMapper;

    @Autowired
    private WmsInboundDetailMapper wmsInventoryOperationDetailMapper;

    @Autowired
    private WmsMaterialServiceImpl wmsMaterialServiceImpl;

    @Autowired
    private WmsStorageLocationServiceImpl wmsStorageLocationServiceImpl;

    @Autowired
    private InventoryUpdateService inventoryUpdateService;

    @Autowired
    private WmsTaskServiceImpl wmsTaskServiceImpl;

    public int insertSelective(WmsInventoryOperation wmsInventoryOperation) {
        if( wmsInventoryOperation.getWmsInventoryOperationDetails() == null || wmsInventoryOperation.getWmsInventoryOperationDetails().isEmpty()) {
            throw new IllegalArgumentException("Inventory operation details are required.");
        }
        // 1. insert wms_inbound table
        Long id = wmsInventoryOperation.getId();
        if (id == null) {
            id = IdWorker.getId();
            wmsInventoryOperation.setId(id);
        }
        int rows_affected = wmsInventoryOperationMapper.insertSelective(wmsInventoryOperation);

        if ("PDA".equals(wmsInventoryOperation.getSource())) {

            // for PDA, there are only RFID in Operation Detail, each RFID mapping to 1 material and 1 quantity, need summarize first
            Map<Long, WmsInventoryOperationDetail> material_id_detail_map = new HashMap<>();
            wmsInventoryOperation.getWmsInventoryOperationDetails().forEach(wmsInventoryOperationDetail -> {

                WmsRfidMaterial wmsRfidMaterial = new WmsRfidMaterial();
                wmsRfidMaterial.setRf_id(wmsInventoryOperationDetail.getRf_id());
                List<WmsRfidMaterial> wmsRfidMaterials = wmsRfidMaterialServiceImpl.selectall(wmsRfidMaterial);

                if (!wmsRfidMaterials.isEmpty()) {
                    wmsRfidMaterial = wmsRfidMaterials.get(0);
                    Long material_id = wmsRfidMaterial.getMaterial_id();
                    if (material_id_detail_map.containsKey(material_id)) {
                        WmsInventoryOperationDetail detail = material_id_detail_map.get(material_id);
                        detail.setQuantity(detail.getQuantity() + 1);
                    } else {
                        WmsInventoryOperationDetail detail = new WmsInventoryOperationDetail();
                        material_id_detail_map.put(material_id, detail);
                    }
                }
            });
            if (!material_id_detail_map.isEmpty()) {
                wmsInventoryOperation.setWmsInventoryOperationDetails((List<WmsInventoryOperationDetail>) material_id_detail_map.values());
            }
        }

        // 2. insert wms_inbound_detail table
        for (WmsInventoryOperationDetail detail : wmsInventoryOperation.getWmsInventoryOperationDetails()) {
            detail.setOperation_id(id);
            wmsInventoryOperationDetailMapper.insertSelective(detail);
        }

        // 3. update wms_storage_location table, assume 1 storage location only store 1 kind of material
        wmsInventoryOperation.getWmsInventoryOperationDetails().forEach(wmsInventoryOperationDetail -> {
            WmsMaterial wmsMaterial = new WmsMaterial();
            wmsMaterial.setId(wmsInventoryOperationDetail.getMaterial_id());
            List<WmsMaterial> wmsMaterials = wmsMaterialServiceImpl.selectAll(wmsMaterial);
            if(!wmsMaterials.isEmpty()) {
                WmsStorageLocation wmsStorageLocation = new WmsStorageLocation();
                wmsStorageLocation.setId(wmsInventoryOperationDetail.getLocation_id());
                wmsStorageLocation.setMaterial_name(wmsMaterials.get(0).getName());
                wmsStorageLocation.setQuantity(wmsInventoryOperationDetail.getQuantity());
                wmsStorageLocationServiceImpl.updateStorageLocationById(wmsStorageLocation);
            }
        });

        // 4. update wms_material_storage_location table
        for (WmsInventoryOperationDetail wmsInventoryOperationDetail : wmsInventoryOperation.getWmsInventoryOperationDetails()) {
            inventoryUpdateService.updateMaterialStorageLocation(wmsInventoryOperationDetail.getMaterial_id(), wmsInventoryOperationDetail.getLocation_id(), wmsInventoryOperationDetail.getQuantity(), true);
        }

        // 5. create put away task
        WmsTask wmsTask = new WmsTask();
        wmsTask.setId(IdWorker.getId());
        wmsTask.setOperation_id(id);
        wmsTask.setType("putaway");
        wmsTask.setNote("Inbound triggered putaway task on " + DateTime.now().toString());
        wmsTask.setStatus("pending");
        wmsTaskServiceImpl.insertSelective(wmsTask);

        // 6. use rule to auto assign people and resources
        inventoryUpdateService.updatePeopleAndResourceByRule(wmsTask);

        return rows_affected;
    }

    public int updateOperationById(WmsInventoryOperation wmsInventoryOperation) {
        return wmsInventoryOperationMapper.updateRecordById(wmsInventoryOperation);
    }

    public int deleteOperationById(WmsInventoryOperation wmsInventoryOperation) {
        return wmsInventoryOperationMapper.deleteRecordById(wmsInventoryOperation);
    }

    public List<WmsInventoryOperation> selectAll(WmsInventoryOperation wmsInventoryOperation) {
        List<WmsInventoryOperation> wmsInventoryOperations = wmsInventoryOperationMapper.selectAll(wmsInventoryOperation);
        for(WmsInventoryOperation operation: wmsInventoryOperations) {
            WmsInventoryOperationDetail wmsInventoryOperationDetail = new WmsInventoryOperationDetail();
            wmsInventoryOperationDetail.setOperation_id(operation.getId());
            List<WmsInventoryOperationDetail> details = wmsInventoryOperationDetailMapper.selectAll(wmsInventoryOperationDetail);
            operation.setWmsInventoryOperationDetails(details);
        }
        return wmsInventoryOperations;
    }

}




