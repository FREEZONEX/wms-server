package com.supos.app.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supos.app.common.config.ApiResponse;
import com.supos.app.domain.entity.WmsStorageLocation;
import com.supos.app.domain.vo.ShelfModel;
import com.supos.app.domain.vo.WarehouseNamemap;
import com.supos.app.service.impl.*;
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

    @ApiOperation(value = "storage-location/add", notes = "storage-location/add")
    @PostMapping("/wms/storage-location/add")
    public ApiResponse<Map<String, String>> storagelocationInsert(@RequestBody(required = false) WmsStorageLocation wmsStorageLocation) {
        Map<String, String> responseData = new HashMap<>();
        try {
            responseData.put("rows_affected", String.valueOf(wmsStorageLocationServiceImpl.insertSelective(wmsStorageLocation)));
            return new ApiResponse<>(responseData);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "storage-location/update", notes = "storage-location/update")
    @PostMapping("/wms/storage-location/update")
    public ApiResponse<Map<String, String>> storagelocationUpdate(@RequestBody(required = false) WmsStorageLocation wmsStorageLocation) {
        Map<String, String> responseData = new HashMap<>();
        try {
            responseData.put("rows_affected", String.valueOf(wmsStorageLocationServiceImpl.updateStorageLocationById(wmsStorageLocation)));
            return new ApiResponse<>(responseData);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "storage-location/delete", notes = "storage-location/delete")
    @PostMapping("/wms/storage-location/delete")
    public ApiResponse<Map<String, String>> storagelocationDelete(@RequestBody(required = false) WmsStorageLocation wmsStorageLocation) {
        Map<String, String> responseData = new HashMap<>();
        try {
            responseData.put("rows_affected", String.valueOf(wmsStorageLocationServiceImpl.deleteStorageLocationById(wmsStorageLocation)));
            return new ApiResponse<>(responseData);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "storage-location/get", notes = "storage-location/get")
    @PostMapping("/wms/storage-location/get")
    public ApiResponse<PageInfo<WmsStorageLocation>> storagelocationSelectAll(@RequestBody(required = false) WmsStorageLocation wmsStorageLocation, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        try {
            PageInfo<WmsStorageLocation> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> wmsStorageLocationServiceImpl.selectAll(wmsStorageLocation));
            return new ApiResponse<>(pageInfo);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "storage-location/namemap", notes = "storage-location/namemap")
    @PostMapping("/wms/storage-location/namemap")
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
    @PostMapping("/wms/storage-location/plane-locations")
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

            ShelfModel shelfModel = new ShelfModel();
            shelfModel.setPlaneNames(planeNames);
            return new ApiResponse<>(shelfModel);

        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
    }
}