package com.supos.app.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supos.app.entity.*;
import com.supos.app.mapper.WmsMaterialStorageLocationMapper;
import com.supos.app.mapper.WmsStocktakingDetailMapper;
import com.supos.app.mapper.WmsStocktakingMapper;
import com.supos.app.service.InventoryUpdateService;
import com.supos.app.service.WmsStocktakingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class WmsStocktakingServiceImpl extends ServiceImpl<WmsStocktakingMapper, WmsInventoryOperation>
        implements WmsStocktakingService {

    @Autowired
    WmsRfidMaterialServiceImpl wmsRfidMaterialServiceImpl;
    @Autowired
    private WmsStocktakingMapper wmsInventoryOperationMapper;

    @Autowired
    private WmsStocktakingDetailMapper wmsInventoryOperationDetailMapper;

    @Autowired
    private WmsMaterialStorageLocationMapper wmsMaterialStorageLocationMapper;

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
        // 1. insert wms_stocktaking table
        Long id = wmsInventoryOperation.getId();
        if (id == null) {
            id = IdWorker.getId();
            wmsInventoryOperation.setId(id);
        }
        int rows_affected = wmsInventoryOperationMapper.insertSelective(wmsInventoryOperation);

        // 2. insert wms_stocktaking_detail table
        for (WmsInventoryOperationDetail detail : wmsInventoryOperation.getWmsInventoryOperationDetails()) {
            detail.setOperation_id(id);
            wmsInventoryOperationDetailMapper.insertSelective(detail);
        }

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

            // calculate stocktaking result
            for(WmsInventoryOperationDetail detail: details) {

                WmsMaterial wmsMaterial = new WmsMaterial();
                wmsMaterial.setId(detail.getMaterial_id());
                List<WmsMaterial> wmsMaterials = wmsMaterialServiceImpl.selectAll(wmsMaterial);
                if(!wmsMaterials.isEmpty()) {
                    detail.setMaterial_name(wmsMaterials.get(0).getName());
                }

                WmsMaterialStorageLocation wmsMaterialStorageLocation = new WmsMaterialStorageLocation();
                wmsMaterialStorageLocation.setLocation_id(detail.getLocation_id());
                wmsMaterialStorageLocation.setMaterial_id(detail.getMaterial_id());
                List<WmsMaterialStorageLocation> wmsMaterialStorageLocations = wmsMaterialStorageLocationMapper.selectAll(wmsMaterialStorageLocation);

                int quantity = detail.getQuantity();
                int stock_quantity = 0;
                if(!wmsMaterialStorageLocations.isEmpty()) {
                    stock_quantity = wmsMaterialStorageLocations.get(0).getQuantity();
                }
                detail.setStock_quantity(stock_quantity);
                detail.setDiscrepancy(stock_quantity - quantity);
            }
            operation.setWmsInventoryOperationDetails(details);
        }
        return wmsInventoryOperations;
    }

}




