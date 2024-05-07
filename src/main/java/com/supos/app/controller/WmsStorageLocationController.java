package com.supos.app.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supos.app.config.ApiResponse;
import com.supos.app.vo.ShelfModel;
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

@Api(value = "Storage Location API", tags = {"02. Storage Location"})

@Slf4j
@RestController
public class WmsStorageLocationController {

    @Autowired
    WmsStorageLocationServiceImpl wmsStorageLocationServiceImpl;

    @Autowired
    WmsMaterialServiceImpl wmsMaterialServiceImpl;

    @Autowired
    WmsMaterialTransactionServiceImpl wmsMaterialTransactionServiceImpl;

    @ApiOperation(value = "storagelocation/add", notes = "storagelocation/add")
    @PostMapping("/wms/storagelocation/add")
    public ApiResponse<Map<String, String>> storagelocationInsert(@RequestBody(required = false) WmsStorageLocation wmsStorageLocation) {
        Map<String, String> responseData = new HashMap<>();
        try {
            responseData.put("id", String.valueOf(wmsStorageLocationServiceImpl.insertSelective(wmsStorageLocation)));
            return new ApiResponse<>(responseData);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "storagelocation/update", notes = "storagelocation/update")
    @PostMapping("/wms/storage-location/update")
    public ApiResponse<Map<String, String>> storagelocationUpdate(@RequestBody(required = false) WmsStorageLocation wmsStorageLocation) {
        Map<String, String> responseData = new HashMap<>();
        try {
            responseData.put("id", String.valueOf(wmsStorageLocationServiceImpl.updateStorageLocationById(wmsStorageLocation)));
            return new ApiResponse<>(responseData);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "storagelocation/delete", notes = "storagelocation/delete")
    @PostMapping("/wms/storage-location/delete")
    public ApiResponse<Map<String, String>> storagelocationDelete(@RequestBody(required = false) ID id) {
        Map<String, String> responseData = new HashMap<>();
        try {
            WmsStorageLocation wmsStorageLocation = new WmsStorageLocation();
            wmsStorageLocation.setId(id.getID());
            responseData.put("id", String.valueOf(wmsStorageLocationServiceImpl.deleteStorageLocationById(wmsStorageLocation)));
            return new ApiResponse<>(responseData);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "storagelocation/get", notes = "storagelocation/get")
    @PostMapping("/wms/storage-location/get")
    public ApiResponse<PageInfo<StorageLocationSelectAllResponse>> storagelocationSelectAll(@RequestBody(required = false) WmsStorageLocation wmsStorageLocation, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {

        try {
            PageInfo<WmsStorageLocation> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> wmsStorageLocationServiceImpl.selectAll(wmsStorageLocation));

            List<StorageLocationSelectAllResponse> StorageLocationSelectAllResponses = pageInfo.getList().stream().map(
                    storageLocation -> {
                        StorageLocationSelectAllResponse storageLocationSelectAllResponse = new StorageLocationSelectAllResponse(storageLocation);

                        WmsMaterialTransaction materialTransactionquery = new WmsMaterialTransaction();
                        materialTransactionquery.setWarehouse_id(storageLocation.getWarehouse_id());
                        materialTransactionquery.setStock_location_id(storageLocation.getId());

                        List<WmsMaterialTransaction> MaterialTransactions = wmsMaterialTransactionServiceImpl.selectAllGroupByMaterialCode(materialTransactionquery);

                        List<StorageLocationSelectAllMaterial> storageLocationMaterials = MaterialTransactions.stream()
                                .map(transaction -> {
                                    StorageLocationSelectAllMaterial locationMaterial = new StorageLocationSelectAllMaterial(transaction);
                                    WmsMaterial wmsMaterial = new WmsMaterial();
                                    wmsMaterial.setProduct_code(transaction.getMaterial_code());
                                    List<WmsMaterial> materials = wmsMaterialServiceImpl.selectAll(wmsMaterial);
                                    if (!materials.isEmpty()) {
                                        locationMaterial.setMaterial_name(materials.get(0).getName());
                                    }
                                    return locationMaterial;
                                })
                                .collect(Collectors.toList());

                        storageLocationSelectAllResponse.setMaterials(storageLocationMaterials);
                        return storageLocationSelectAllResponse;
                    }
            ).collect(Collectors.toList());
            PageInfo<StorageLocationSelectAllResponse> responsePageInfo = new PageInfo<>(StorageLocationSelectAllResponses);
            BeanUtils.copyProperties(pageInfo, responsePageInfo, "list"); // Copy pagination details except the list
            return new ApiResponse<>(responsePageInfo);

        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "storagelocation/namemap", notes = "storagelocation/namemap")
    @PostMapping("/wms/storagelocation/namemap")
    public ApiResponse<PageInfo<WarehouseNamemap>> storagelocationNamemap(@RequestBody(required = false) WmsStorageLocation wmsStorageLocation, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        try {
            PageInfo<WmsStorageLocation> pageInfo = PageHelper.startPage(pageNum, pageSize)
                    .doSelectPageInfo(() -> wmsStorageLocationServiceImpl.selectAll(wmsStorageLocation));
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

    // Helper method to convert a number to Excel column style (e.g., 27 -> "AA")
    private static String getColumnLabel(int index) {
        StringBuilder label = new StringBuilder();
        while (index >= 0) {
            int remainder = index % 26;
            label.insert(0, (char)('A' + remainder));
            index = (index / 26) - 1;
        }
        return label.toString();
    }

    @ApiOperation(value = "Fetch Plane Location Information", notes = "Get an overhead view of the warehouse, return {{'A-01', 'A-02', 'A-03', 'A-04', 'A-05'}, {'B-01', 'B-02', 'B-03', 'B-04', 'B-05'}}")
    @PostMapping("/wms/storagelocation/plane-locations")
    public ApiResponse<ShelfModel> getPlaneLocations(@RequestBody(required = false) WmsStorageLocation wmsStorageLocation) {
        try {
            Map<String, List<String>> planeNames = new HashMap<String, List<String>>();
            int totalElements = 40;  // Set this to how many elements you want
            int index = 0;

            // Generate labels for shelves
            for (int i = 0; i < totalElements; i++) {
                String prefix = getColumnLabel(index);
                List<String> subList = new ArrayList<>();
                for (int j = 1; j <= 5; j++) {
                    subList.add(prefix + "-0" + j);
                }
                planeNames.put(prefix, subList);
                index++;
            }

            //PageInfo<ShelfModel> responsePageInfo = new PageInfo<ShelfModel>(shelfModels);
            ShelfModel shelfModel = new ShelfModel();
            shelfModel.setPlaneNames(planeNames);
            return new ApiResponse<>(shelfModel);

        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
    }
}