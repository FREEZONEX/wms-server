package com.supos.app.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supos.app.common.config.ApiResponse;
import com.supos.app.domain.entity.WmsInventoryOperation;
import com.supos.app.service.impl.WmsStocktakingServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Api(value = "Stocktaking API", tags = {"07. Stocktaking"})

@Slf4j
@RestController
public class WmsStocktakingController {

    @Autowired
    WmsStocktakingServiceImpl wmsStocktakingServiceImpl;

    @ApiOperation(value = "stocktaking/add", notes = "stocktaking/add")
    @PostMapping("/wms/stocktaking/add")
    public ApiResponse<Map<String, String>> stocktakingInsert(@RequestBody(required = false) WmsInventoryOperation wmsInventoryOperation) {
        Map<String, String> responseData = new HashMap<>();
        try {
            responseData.put("rows_affected", String.valueOf(wmsStocktakingServiceImpl.insertSelective(wmsInventoryOperation)));
            return new ApiResponse<>(responseData);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ApiResponse<>( null,"Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "stocktaking/update", notes = "stocktaking/update")
    @PostMapping("/wms/stocktaking/update")
    public ApiResponse<Map<String, String>> stocktakingUpdate(@RequestBody(required = false) WmsInventoryOperation wmsInventoryOperation) {
        Map<String, String> responseData = new HashMap<>();
        try {
            responseData.put("rows_affected", String.valueOf(wmsStocktakingServiceImpl.updateOperationById(wmsInventoryOperation)));
            return new ApiResponse<>(responseData);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ApiResponse<>( null,"Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "stocktaking/delete", notes = "stocktaking/delete")
    @PostMapping("/wms/stocktaking/delete")
    public ApiResponse<Map<String, String>> stocktakingDelete(@RequestBody(required = false) WmsInventoryOperation wmsInventoryOperation) {
        Map<String, String> responseData = new HashMap<>();
        try {
            responseData.put("rows_affected", String.valueOf(wmsStocktakingServiceImpl.deleteOperationById(wmsInventoryOperation)));
            return new ApiResponse<>(responseData);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ApiResponse<>( null,"Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "stocktaking/get", notes = "stocktaking/get")
    @PostMapping("/wms/stocktaking/get")
    public ApiResponse<PageInfo<WmsInventoryOperation>> stocktakingGet(@RequestBody(required = false) WmsInventoryOperation wmsInventoryOperation, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        try {
            return new ApiResponse<>(PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() ->  wmsStocktakingServiceImpl.selectAll(wmsInventoryOperation)));
        }catch (Exception e){
            log.info(e.getMessage());
            return new ApiResponse<>( null,"Error occurred while processing the request: " + e.getMessage());
        }
    }

}