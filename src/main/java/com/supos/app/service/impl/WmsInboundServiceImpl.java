package com.supos.app.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.supos.app.domain.entity.*;
import com.supos.app.mapper.*;
import com.supos.app.service.WmsInboundService;
import com.supos.app.service.InventoryUpdateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Autowired
    private MqttServiceImpl mqttServiceImpl;

    @Value("${mqtt.topic_increment}")
    private String mqttTopicIncrement;

    private String checkInboundCondition(WmsInventoryOperation wmsInventoryOperation) {
        for(WmsInventoryOperationDetail wmsInventoryOperationDetail : wmsInventoryOperation.getWmsInventoryOperationDetails()) {
            Long material_id = wmsInventoryOperationDetail.getMaterial_id();
            Integer quantity = wmsInventoryOperationDetail.getQuantity();
            if( material_id == null || quantity == null || quantity <= 0 ) {
                return "Material ID and quantity are required.";
            }
            WmsMaterial wmsMaterial = new WmsMaterial();
            wmsMaterial.setId(material_id);
            List<WmsMaterial> wmsMaterials = wmsMaterialServiceImpl.selectAll(wmsMaterial);
            if(!wmsMaterials.isEmpty()) {
                wmsMaterial = wmsMaterials.get(0);
                Long max_quantity = wmsMaterial.getMax();
                int stock_quantity = inventoryUpdateService.GetMaterialQuantity(material_id);
                if( stock_quantity + quantity > max_quantity) {
                    return "Insufficient stock for material: " + material_id + ", stock quantity: " + stock_quantity + ", maximum quantity: " + max_quantity + ", required inbound quantity: " + quantity;
                }
            }
            return "material not found: " + material_id;
        }
        return "";
    }

    @Transactional
    public int insertSelective(WmsInventoryOperation wmsInventoryOperation) {
        if( wmsInventoryOperation.getWmsInventoryOperationDetails() == null || wmsInventoryOperation.getWmsInventoryOperationDetails().isEmpty()) {
            throw new IllegalArgumentException("Inventory operation details are required.");
        }
        if( !"PDA".equals(wmsInventoryOperation.getSource()) &&  !"manual".equals(wmsInventoryOperation.getSource()) ) {
            throw new IllegalArgumentException("source must be PDA or manual");
        }
        String checkResult = checkInboundCondition(wmsInventoryOperation);
        if( checkResult != "" ) {
            throw new IllegalArgumentException(checkResult);
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
        List<WmsStorageLocation> wmsStorageLocations = new ArrayList<>();
        for (WmsInventoryOperationDetail wmsInventoryOperationDetail : wmsInventoryOperation.getWmsInventoryOperationDetails()) {
            WmsStorageLocation wmsStorageLocation = inventoryUpdateService.updateStorageLocationMaterial(wmsInventoryOperationDetail.getLocation_id(), wmsInventoryOperationDetail.getMaterial_id(), wmsInventoryOperationDetail.getQuantity(), true);
            wmsStorageLocations.add(wmsStorageLocation);
        }

        // 4. update wms_material_storage_location table
        for (WmsInventoryOperationDetail wmsInventoryOperationDetail : wmsInventoryOperation.getWmsInventoryOperationDetails()) {
            inventoryUpdateService.updateMaterialStorageLocation(wmsInventoryOperationDetail.getMaterial_id(), wmsInventoryOperationDetail.getLocation_id(), wmsInventoryOperationDetail.getQuantity(), true);
        }

        // 5. create putaway task
        WmsTask wmsTask = new WmsTask();
        wmsTask.setId(IdWorker.getId());
        wmsTask.setOperation_id(id);
        wmsTask.setType("putaway");
        wmsTask.setNote("Inbound triggered putaway task on " + DateTime.now());
        wmsTask.setStatus("pending");
        wmsTaskServiceImpl.insertSelective(wmsTask);

        // 6. use rule to auto assign people and resources
        List<String> resources = inventoryUpdateService.updatePeopleAndResourceByRule(wmsTask);

        // 7. send mqtt message to unity for increment
        mqttServiceImpl.sendIncrementToUnity(wmsStorageLocations, resources, true);

        // 8. send mqtt message to AI for full data
        Long warehouse_id = wmsStorageLocations.get(0).getWarehouse_id();
        mqttServiceImpl.sendIncrementFullToAI(warehouse_id);

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




