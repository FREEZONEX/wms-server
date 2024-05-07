package com.supos.app.controller;

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

@Api(value = "Warehouse API", tags = {"01. Warehouse"})

@Slf4j
@RestController
public class WmsWarehouseController {

    @Autowired
    WmsWarehouseServiceImpl wmsWarehouseServiceImpl;

    @Autowired
    WmsStorageLocationServiceImpl wmsStorageLocationServiceImpl;

    @ApiOperation(value = "warehouse/add", notes = "warehouse/add")
    @PostMapping("/wms/warehouse/add")
    public ApiResponse<Map<String, String>> warehouseInsert(@RequestBody(required = false) WmsWarehouse wmsWarehouse) {
        Map<String, String> responseData = new HashMap<>();
        try {
            responseData.put("id", String.valueOf(wmsWarehouseServiceImpl.insertSelective(wmsWarehouse)));
            return new ApiResponse<>(responseData);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "warehouse/update", notes = "warehouse/update")
    @PostMapping("/wms/warehouse/update")
    public ApiResponse<Map<String, String>> warehouseUpdate(@RequestBody(required = false) WmsWarehouse wmsWarehouse) {
        Map<String, String> responseData = new HashMap<>();
        try {
            responseData.put("id", String.valueOf(wmsWarehouseServiceImpl.updateWarehouseById(wmsWarehouse)));
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
        return new ApiResponse<>(responseData);
    }

    @ApiOperation(value = "warehouse/delete", notes = "warehouse/delete")
    @PostMapping("/wms/warehouse/delete")
    public ApiResponse<Map<String, String>> warehouseDelete(@RequestBody(required = false) ID id) {
        Map<String, String> responseData = new HashMap<>();
        try {
            WmsWarehouse wmsWarehouse = new WmsWarehouse();
            wmsWarehouse.setId(id.getID());
            responseData.put("id", String.valueOf(wmsWarehouseServiceImpl.deleteWarehouseById(wmsWarehouse)));
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
        return new ApiResponse<>(responseData);
    }

    @ApiOperation(value = "warehouse/get", notes = "warehouse/get")
    @PostMapping("/wms/warehouse/get")
    public ApiResponse<PageInfo<WarehouseSelectAllResponse>> warehouseSelectAll(@RequestBody(required = false) WmsWarehouse wmsWarehouse, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        try {
            PageInfo<WmsWarehouse> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> wmsWarehouseServiceImpl.selectAll(wmsWarehouse));

            List<WmsWarehouse> wmsWarehouseList = pageInfo.getList();
            List<WarehouseSelectAllResponse> warehouseSelectAllResponses = wmsWarehouseList.stream()
                    .map(warehouse -> {
                        WarehouseSelectAllResponse response = new WarehouseSelectAllResponse(warehouse);
                        return response;
                    })
                    .collect(Collectors.toList());

            PageInfo<WarehouseSelectAllResponse> responsePageInfo = new PageInfo<>(warehouseSelectAllResponses);
            BeanUtils.copyProperties(pageInfo, responsePageInfo, "list"); // Copy pagination details except the list
            return new ApiResponse<>(responsePageInfo);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "warehouse/namemap", notes = "warehouse/namemap")
    @PostMapping("/wms/warehouse/namemap")
    public ApiResponse<PageInfo<WarehouseNamemap>> warehouseNamemap(@RequestBody(required = false) WmsWarehouse wmsWarehouse, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        try {
            PageInfo<WmsWarehouse> pageInfo = PageHelper.startPage(pageNum, pageSize)
                    .doSelectPageInfo(() -> wmsWarehouseServiceImpl.selectAll(wmsWarehouse));
            List<WarehouseNamemap> mappedList = pageInfo.getList().stream()
                    .map(a -> {
                        WarehouseNamemap warehouseNamemapTmp = new WarehouseNamemap();
                        warehouseNamemapTmp.setName(a.getName());
                        warehouseNamemapTmp.setId(a.getId());
                        return warehouseNamemapTmp;
                    })
                    .collect(Collectors.toList());

            PageInfo<WarehouseNamemap> responsePageInfo = new PageInfo<>();
            BeanUtils.copyProperties(pageInfo, responsePageInfo); // Copy pagination details
            responsePageInfo.setList(mappedList); // Set the transformed list

            return new ApiResponse<>(responsePageInfo);

        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "warehouse/storagelocation/idmap", notes = "warehouse/storagelocation/idmap")
    @PostMapping("/wms/warehouse/storagelocation/idmap")
    public ApiResponse<PageInfo<WarehousestoragelocationIdmap>> warehousestoragelocationIdmap(@RequestBody(required = false) WmsStorageLocation wmsStorageLocation, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        try {
            PageInfo<WmsStorageLocation> pageInfo = PageHelper.startPage(pageNum, pageSize)
                    .doSelectPageInfo(() -> wmsStorageLocationServiceImpl.selectAll(wmsStorageLocation));
            List<WarehousestoragelocationIdmap> mappedList = pageInfo.getList().stream()
                    .collect(Collectors.groupingBy(WmsStorageLocation::getWarehouse_id)) // Assuming getWarehouseId is the correct method name.
                    .entrySet().stream()
                    .map(entry -> {
                        WarehousestoragelocationIdmap idmap = new WarehousestoragelocationIdmap();
                        idmap.setId(entry.getKey());
                        WmsWarehouse wmsWarehouse = new WmsWarehouse();
                        wmsWarehouse.setId(entry.getKey());
                        idmap.setName(wmsWarehouseServiceImpl.selectAll(wmsWarehouse).get(0).getName());

                        List<WarehouseNamemap> namemaps = entry.getValue().stream().map(storageLocation -> {
                            WarehouseNamemap namemap = new WarehouseNamemap();
                            namemap.setId(storageLocation.getId());
                            namemap.setName(storageLocation.getName());
                            return namemap;
                        }).collect(Collectors.toList());
                        idmap.setWarehouseNamemap(namemaps); // Assuming setWarehouseNamemap accepts a list of WarehouseNamemap
                        return idmap;
                    })
                    .collect(Collectors.toList());

            PageInfo<WarehousestoragelocationIdmap> responsePageInfo = new PageInfo<>(mappedList);
            BeanUtils.copyProperties(pageInfo, responsePageInfo, "list"); // Copy pagination details except the list
            return new ApiResponse<>(responsePageInfo);

        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
    }
}