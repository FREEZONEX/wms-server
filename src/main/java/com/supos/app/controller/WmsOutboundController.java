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

@Api(value = "Outbound API", tags = {"06. Outbound"})

@Slf4j
@RestController
public class WmsOutboundController {

    @Autowired
    WmsOutboundServiceImpl wmsOutboundServiceImpl;

    @ApiOperation(value = "outbound/add", notes = "outbound/add")
    @PostMapping("/wms/outbound/add")
    public ApiResponse<Map<String, String>> outboundInsert(@RequestBody(required = false) WmsInventoryOperation wmsInventoryOperation) {
        Map<String, String> responseData = new HashMap<>();
        try {
            responseData.put("rows_affected", String.valueOf(wmsOutboundServiceImpl.insertSelective(wmsInventoryOperation)));
            return new ApiResponse<>(responseData);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ApiResponse<>( null,"Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "outbound/update", notes = "outbound/update")
    @PostMapping("/wms/outbound/update")
    public ApiResponse<Map<String, String>> outboundUpdate(@RequestBody(required = false) WmsInventoryOperation wmsInventoryOperation) {
        Map<String, String> responseData = new HashMap<>();
        try {
            responseData.put("rows_affected", String.valueOf(wmsOutboundServiceImpl.updateOperationById(wmsInventoryOperation)));
            return new ApiResponse<>(responseData);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ApiResponse<>( null,"Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "outbound/delete", notes = "outbound/delete")
    @PostMapping("/wms/outbound/delete")
    public ApiResponse<Map<String, String>> outboundDelete(@RequestBody(required = false) WmsInventoryOperation wmsInventoryOperation) {
        Map<String, String> responseData = new HashMap<>();
        try {
            responseData.put("rows_affected", String.valueOf(wmsOutboundServiceImpl.deleteOperationById(wmsInventoryOperation)));
            return new ApiResponse<>(responseData);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ApiResponse<>( null,"Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "outbound/get", notes = "outbound/get")
    @PostMapping("/wms/outbound/get")
    public ApiResponse<PageInfo<WmsInventoryOperation>> outboundGet(@RequestBody(required = false) WmsInventoryOperation wmsInventoryOperation, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        try {
            return new ApiResponse<>(PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() ->  wmsOutboundServiceImpl.selectAll(wmsInventoryOperation)));
        }catch (Exception e){
            log.info(e.getMessage());
            return new ApiResponse<>( null,"Error occurred while processing the request: " + e.getMessage());
        }
    }
    
}