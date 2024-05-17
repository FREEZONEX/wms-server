package com.supos.app.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supos.app.common.config.ApiResponse;
import com.supos.app.domain.entity.WmsInventoryOperation;
import com.supos.app.service.impl.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Api(value = "Inbound API", tags = {"05. Inbound"})

@Slf4j
@RestController
public class WmsInboundController {

    @Autowired
    WmsInboundServiceImpl wmsInboundServiceImpl;

    @ApiOperation(value = "inbound/add", notes = "inbound/add")
    @PostMapping("/wms/inbound/add")
    public ApiResponse<Map<String, String>> inboundInsert(@RequestBody(required = false) WmsInventoryOperation wmsInventoryOperation) {
        Map<String, String> responseData = new HashMap<>();
        try {
            responseData.put("rows_affected", String.valueOf(wmsInboundServiceImpl.insertSelective(wmsInventoryOperation)));
            return new ApiResponse<>(responseData);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ApiResponse<>( null,"Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "inbound/update", notes = "inbound/update")
    @PostMapping("/wms/inbound/update")
    public ApiResponse<Map<String, String>> inboundUpdate(@RequestBody(required = false) WmsInventoryOperation wmsInventoryOperation) {
        Map<String, String> responseData = new HashMap<>();
        try {
            responseData.put("rows_affected", String.valueOf(wmsInboundServiceImpl.updateOperationById(wmsInventoryOperation)));
            return new ApiResponse<>(responseData);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ApiResponse<>( null,"Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "inbound/delete", notes = "inbound/delete")
    @PostMapping("/wms/inbound/delete")
    public ApiResponse<Map<String, String>> inboundDelete(@RequestBody(required = false) WmsInventoryOperation wmsInventoryOperation) {
        Map<String, String> responseData = new HashMap<>();
        try {
            responseData.put("rows_affected", String.valueOf(wmsInboundServiceImpl.deleteOperationById(wmsInventoryOperation)));
            return new ApiResponse<>(responseData);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ApiResponse<>( null,"Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "inbound/get", notes = "inbound/get")
    @PostMapping("/wms/inbound/get")
    public ApiResponse<PageInfo<WmsInventoryOperation>> inboundGet(@RequestBody(required = false) WmsInventoryOperation wmsInventoryOperation, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        try {
            return new ApiResponse<>(PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() ->  wmsInboundServiceImpl.selectAll(wmsInventoryOperation)));
        }catch (Exception e){
            log.info(e.getMessage());
            return new ApiResponse<>( null,"Error occurred while processing the request: " + e.getMessage());
        }
    }

}