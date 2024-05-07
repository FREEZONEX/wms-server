package com.supos.app.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supos.app.config.ApiResponse;
import com.supos.app.entity.*;
import com.supos.app.service.impl.*;
import com.supos.app.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@Api(value = "Stocktaking API", tags = {"07. Stocktaking"})

@Slf4j
@RestController
public class WmsStocktakingController {

    @Autowired
    WmsStorageLocationServiceImpl wmsStorageLocationServiceImpl;

    @Autowired
    WmsMaterialServiceImpl wmsMaterialServiceImpl;

    @Autowired
    WmsMaterialTransactionServiceImpl wmsMaterialTransactionServiceImpl;

    @ApiOperation(value = "stocktaking/add", notes = "stocktaking/add")
    @PostMapping("/wms/stocktaking/add")
    public ApiResponse<Map<String, String>> stocktakingInsert(@RequestBody(required = false) AddInboundRequest addInboundRequest) {
        try {
            Map<String, String> responseData = new HashMap<>();
            long ID = IdWorker.getId();
            addInboundRequest.getShelfRecords().forEach(
                    i -> {
                        i.getInventory().forEach(
                                b -> {
                                    if (b.getQuantity() > 0) {
                                        int tmp = wmsMaterialTransactionServiceImpl.updateForTopNTransactionsStocktaking(ID, b.getMaterialCode(), b.getQuantity(), i.getStorageLocationId());
                                        if (b.getQuantity() - tmp == 0) {
                                            responseData.put("id", String.valueOf(ID));
                                        } else {
                                            for (int j = 0; j < b.getQuantity() - tmp; j++) {
                                                WmsMaterialTransaction wmsMaterialTransaction = new WmsMaterialTransaction();
                                                wmsMaterialTransaction.setStocktaking_id(ID);
                                                wmsMaterialTransaction.setMaterial_code(b.getMaterialCode());
                                                wmsMaterialTransaction.setStock_location_id(Long.valueOf(i.getStorageLocationId()));
                                                wmsMaterialTransactionServiceImpl.insertSelective(wmsMaterialTransaction);
                                                responseData.put("id", String.valueOf(b.getQuantity()));
                                            }
                                        }
                                    }
                                }
                        );
                    }
            );
            return new ApiResponse<>(responseData);
        } catch (Exception e) {
            log.error("Error occurred while processing the request: " + e.getMessage(), e);
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "stocktaking/update", notes = "stocktaking/update")
    @PostMapping("/wms/stocktaking/update")
    public ApiResponse<Map<String, String>> stocktakingUpdate(@RequestBody(required = false) AddInboundRequest addInboundRequest) {
        try {
            Map<String, String> responseData = new HashMap<>();
            addInboundRequest.getShelfRecords().stream().forEach(
                    i->{
                        i.getInventory().stream().forEach(
                                b->{
                                    wmsMaterialTransactionServiceImpl.updateForTopNTransactionsStocktaking(IdWorker.getId(),b.getMaterialCode(),b.getQuantity(),i.getStorageLocationId());
                                }
                        );
                    }
            );
            responseData.put("id", "1");
            return new ApiResponse<>(responseData);
        } catch (Exception e) {
            log.error("Error occurred while processing the request: " + e.getMessage(), e);
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "stocktaking/delete", notes = "stocktaking/delete")
    @PostMapping("/wms/stocktaking/delete")
    public ApiResponse<Map<String, String>> stocktakingDelete(@RequestBody(required = false) ID id) {
        try {
            Map<String, String> responseData = new HashMap<>();
            wmsMaterialTransactionServiceImpl.deleteForTopNTransactionsStocktaking(id.getID());
            responseData.put("id", "1");
            return new ApiResponse<>(responseData);
        } catch (Exception e) {
            log.error("Error occurred while processing the request: " + e.getMessage(), e);
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "stocktaking/get", notes = "stocktaking/get")
    @PostMapping("/wms/stocktaking/get")
    public ApiResponse<PageInfo<StocktakingRequest>> stocktakingGet(@RequestBody(required = false) GetStocktakingRequest getStocktakingRequest, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        try {

            WmsMaterialTransaction wmsMaterialTransaction = new WmsMaterialTransaction();
            wmsMaterialTransaction.setStocktaking_id(getStocktakingRequest.getID());
            wmsMaterialTransaction.setRf_id(getStocktakingRequest.getRfid());
            wmsMaterialTransaction.setType(getStocktakingRequest.getType());

            PageInfo<WmsMaterialTransaction> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> wmsMaterialTransactionServiceImpl.selectAllGroupByStocktakingId(wmsMaterialTransaction,getStocktakingRequest.getWarehouseName()));

            List<StocktakingRequest> stocktakingRequestList = pageInfo.getList().stream().map(StocktakingRequest::new).collect(Collectors.toList());

            PageInfo<StocktakingRequest> responsePageInfo = new PageInfo<>(stocktakingRequestList);
            BeanUtils.copyProperties(pageInfo, responsePageInfo, "list"); // Copy pagination details except the list
            return new ApiResponse<>(responsePageInfo);
        } catch (Exception e) {
            log.error("Error occurred while processing the request: " + e.getMessage(), e);
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "stocktaking/get/detail", notes = "stocktaking/get/detail")
    @PostMapping("/wms/stocktaking/detail")
    public ApiResponse<PageInfo<ShelfInventory>> stocktakingDetailGet(@RequestBody(required = false) GetStocktakingRequest getStocktakingRequest, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        try {

            WmsMaterialTransaction wmsMaterialTransaction = new WmsMaterialTransaction();
            wmsMaterialTransaction.setStocktaking_id(getStocktakingRequest.getID());
            wmsMaterialTransaction.setRf_id(getStocktakingRequest.getRfid());

            PageInfo<WmsMaterialTransaction> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> wmsMaterialTransactionServiceImpl.selectAllGroupByStocktakingId(wmsMaterialTransaction, getStocktakingRequest.getWarehouseName()));

            List<ShelfInventory> shelfInventoryList = pageInfo.getList()
                    .stream()
                    .filter(transaction -> transaction.getStock_location_id() != null) // 过滤掉stock_location_id为null的记录
                    .collect(Collectors.groupingBy(WmsMaterialTransaction::getStock_location_id)) // 根据stock_location_id进行分组
                    .entrySet().stream()
                    .map(entry -> {
                        ShelfInventory shelfInventory = new ShelfInventory();
                        shelfInventory.setStorageLocationId(String.valueOf(entry.getKey())); // 设置storage_location_id

                        // 获取对应的storage location名称
                        WmsStorageLocation wmsStorageLocation = new WmsStorageLocation();
                        wmsStorageLocation.setId(entry.getKey());
                        List<WmsStorageLocation> wmsStorageLocationList = wmsStorageLocationServiceImpl.selectAll(wmsStorageLocation);
                        if (!wmsStorageLocationList.isEmpty()) {
                            shelfInventory.setStorageLocation(wmsStorageLocationList.get(0).getName());
                        }

                        // 将分组内的每个物料数据转换为Inventory对象
                        List<Inventory> inventoryList = entry.getValue().stream()
                                .map(transaction -> {
                                    Inventory inventory = new Inventory();
                                    inventory.setMaterialCode(String.valueOf(transaction.getMaterial_code()));
                                    inventory.setQuantity(wmsMaterialTransactionServiceImpl.getQuantityForInbound(inventory.getRfid(), inventory.getMaterialCode(), String.valueOf(entry.getKey())));
                                    inventory.setStockQuantity(wmsMaterialTransactionServiceImpl.getQuantityForStocktaking(inventory.getRfid(), inventory.getMaterialCode(), String.valueOf(entry.getKey())));
                                    inventory.setDiscrepancy(wmsMaterialTransactionServiceImpl.getQuantityForStocktaking(inventory.getRfid(), inventory.getMaterialCode(), String.valueOf(entry.getKey())) - wmsMaterialTransactionServiceImpl.getQuantityForInbound(inventory.getRfid(), inventory.getMaterialCode(), String.valueOf(entry.getKey())));

                                    WmsMaterial wmsMaterial = new WmsMaterial();
                                    wmsMaterial.setProduct_code(transaction.getMaterial_code());
                                    List<WmsMaterial> wmsMaterialList = wmsMaterialServiceImpl.selectAll(wmsMaterial);
                                    if (!wmsMaterialList.isEmpty()) {
                                        WmsMaterial material = wmsMaterialList.get(0);
                                        inventory.setMaterialName(material.getName());
                                    }
                                    inventory.setRfid(transaction.getRf_id());
                                    return inventory;
                                })
                                .collect(Collectors.toList());

                        shelfInventory.setInventory(inventoryList);
                        return shelfInventory;
                    })
                    .collect(Collectors.toList());

            PageInfo<ShelfInventory> responsePageInfo = new PageInfo<>(shelfInventoryList);
            BeanUtils.copyProperties(pageInfo, responsePageInfo, "list"); // Copy pagination details except the list
            return new ApiResponse<>(responsePageInfo);
        } catch (Exception e) {
            log.error("Error occurred while processing the request: " + e.getMessage(), e);
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
    }
}