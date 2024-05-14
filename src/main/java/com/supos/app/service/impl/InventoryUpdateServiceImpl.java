package com.supos.app.service.impl;

import com.supos.app.entity.*;
import com.supos.app.mapper.WmsMaterialStorageLocationMapper;
import com.supos.app.mapper.WmsTaskMapper;
import com.supos.app.mapper.WmsTaskResourceMapper;
import com.supos.app.service.InventoryUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class InventoryUpdateServiceImpl implements InventoryUpdateService {

    @Autowired
    private WmsMaterialStorageLocationMapper wmsMaterialStorageLocationMapper;

    @Autowired
    private WmsRuleServiceImpl wmsRuleServiceImpl;

    @Autowired
    private WmsStorageLocationServiceImpl wmsStorageLocationServiceImpl;

    @Autowired
    private WmsInboundServiceImpl wmsInboundServiceImpl;

    //@Autowired
    //private WmsOutboundServiceImpl wmsOutboundServiceImpl;

    @Autowired
    private WmsTaskServiceImpl wmsTaskServiceImpl;

    @Autowired
    private WmsTaskResourceServiceImpl wmsTaskResourceServiceImpl;

    @Transactional
    @Override
    public void updateMaterialStorageLocation(Long material_id, Long location_id, int quantity, boolean isInbound) {

        if (quantity == 0)
            return;

        WmsMaterialStorageLocation wmsMaterialStorageLocation = new WmsMaterialStorageLocation();
        wmsMaterialStorageLocation.setMaterial_id(material_id);
        wmsMaterialStorageLocation.setLocation_id(location_id);

        List<WmsMaterialStorageLocation> wmsMaterialStorageLocations = wmsMaterialStorageLocationMapper.selectAll(wmsMaterialStorageLocation);

        // if not exist, will add record
        if (wmsMaterialStorageLocations.isEmpty()) {
            wmsMaterialStorageLocation.setQuantity(quantity);
            wmsMaterialStorageLocationMapper.insertSelective(wmsMaterialStorageLocation);
        } else {
            int existQuantity = wmsMaterialStorageLocations.get(0).getQuantity();
            int updatedQuantity = isInbound ? existQuantity + quantity : existQuantity - quantity;
            if (!isInbound && updatedQuantity < 0) {
                throw new IllegalArgumentException("Insufficient stock for outbound operation on material: " + material_id + " at location: " + location_id + ", exist quantity: " + existQuantity + ", required quantity: " + quantity);
            }
            wmsMaterialStorageLocation = wmsMaterialStorageLocations.get(0);
            if (updatedQuantity == 0) {
                wmsMaterialStorageLocationMapper.deleteMaterialStorageLocationById(wmsMaterialStorageLocation);     //remove record if reduce to 0
            } else {
                wmsMaterialStorageLocation.setQuantity(updatedQuantity);
                wmsMaterialStorageLocationMapper.updateMaterialStorageLocationById(wmsMaterialStorageLocation);     // update quantity
            }
        }
    }

    @Transactional
    @Override
    public void updatePeopleAndResourceByRule(WmsTask wmsTask)
    {
        // 1. get task detail
        String task_type = wmsTask.getType();
        Long operation_id = wmsTask.getOperation_id();
        WmsInventoryOperation wmsInventoryOperation = new WmsInventoryOperation();
        wmsInventoryOperation.setId(operation_id);
        List<WmsInventoryOperation> wmsInventoryOperations = new ArrayList<>();
        switch (task_type) {
            case "putaway":
                wmsInventoryOperations = wmsInboundServiceImpl.selectAll(wmsInventoryOperation);
                break;
            case "pickup":
                //wmsInventoryOperations = wmsOutboundServiceImpl.selectAll(wmsInventoryOperation);
                break;
            default:
                return;
        }

        // 2. decide resource allocation by task detail
        if (!wmsInventoryOperations.isEmpty()) {
            wmsInventoryOperation = wmsInventoryOperations.get(0);
            List<WmsInventoryOperationDetail> details = wmsInventoryOperation.getWmsInventoryOperationDetails();
            for (WmsInventoryOperationDetail detail : details) {
                // 3. get location name first
                Long location_id = detail.getLocation_id();
                WmsStorageLocation wmsStorageLocation = new WmsStorageLocation();
                wmsStorageLocation.setId(location_id);
                List<WmsStorageLocation> locations = wmsStorageLocationServiceImpl.selectAll(wmsStorageLocation);
                if (locations.isEmpty())
                    return;
                Long warehouse_id = locations.get(0).getWarehouse_id();
                String location_name = locations.get(0).getName();

                //4. find first match rule for this location
                WmsRule wmsRule = new WmsRule();
                wmsRule.setTask_type(wmsTask.getType());
                wmsRule.setWarehouse_id(warehouse_id);
                wmsRule.setEnabled(true);
                List<WmsRule> rules =  wmsRuleServiceImpl.selectAll(wmsRule);
                for (WmsRule rule : rules) {
                    String location_expression = rule.getLocation_expression();      // regular expression
                    Pattern pattern = Pattern.compile(location_expression);
                    Matcher matcher = pattern.matcher(location_name);
                    if(!matcher.matches())
                        continue;

                    //found matched rule
                    String people_name = rule.getPeople_name();
                    String resource_id_list = rule.getResource_id_list();

                    // 5. assign resource by first matched rule
                    wmsTask.setPeople_name(people_name);
                    int rows_affected = wmsTaskServiceImpl.updateTaskById(wmsTask);
                    System.out.println(rows_affected);
                    String[] resource_ids = resource_id_list.split(",");

                    for (String resource_id : resource_ids) {
                        WmsTaskResource wmsTaskResource = new WmsTaskResource();
                        wmsTaskResource.setTask_id(wmsTask.getId());
                        wmsTaskResource.setResource_id(Long.parseLong(resource_id));
                        wmsTaskResourceServiceImpl.insertSelective(wmsTaskResource, true, true);
                    }
                    break;          // find first matched rule, then break
                }
            }
        }

    }
}