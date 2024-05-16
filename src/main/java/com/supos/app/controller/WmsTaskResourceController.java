package com.supos.app.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supos.app.common.config.ApiResponse;
import com.supos.app.domain.entity.WmsTaskResource;
import com.supos.app.service.impl.WmsTaskResourceServiceImpl;
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

@Api(value = "TaskResource Resource API", tags = {"10.1. TaskResource Resource"})

@Slf4j
@RestController
public class WmsTaskResourceController {

    @Autowired
    WmsTaskResourceServiceImpl wmsTaskResourceServiceImpl;

    @ApiOperation(value = "taskresource/add",notes = "taskresource/add")
    @PostMapping("/wms/taskresource/add")
    public ApiResponse<Map<String, String>> taskresourceInsert(@RequestBody(required = false) WmsTaskResource wmsTaskResource) {
        Map<String, String> responseData = new HashMap<>();
        try {
            responseData.put("rows_affected", String.valueOf(wmsTaskResourceServiceImpl.insertSelective(wmsTaskResource, false, true)));
            return new ApiResponse<>(responseData);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ApiResponse<>( null,"Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "taskresource/update",notes = "taskresource/update")
    @PostMapping("/wms/taskresource/update")
    public ApiResponse<Map<String, String>> taskresourceUpdate(@RequestBody(required = false) WmsTaskResource wmsTaskResource) {
        Map<String, String> responseData = new HashMap<>();
        try {
            responseData.put("rows_affected", String.valueOf(wmsTaskResourceServiceImpl.updateSelective(wmsTaskResource)));
            return new ApiResponse<>(responseData);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ApiResponse<>( null,"Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "taskresource/delete",notes = "taskresource/delete")
    @PostMapping("/wms/taskresource/delete")
    public ApiResponse<Map<String, String>> taskresourceDelete(@RequestBody(required = false) WmsTaskResource wmsTaskResource) {
        Map<String, String> responseData = new HashMap<>();
        try {
            responseData.put("rows_affected", String.valueOf(wmsTaskResourceServiceImpl.deleteTaskResourceById(wmsTaskResource)));
            return new ApiResponse<>(responseData);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ApiResponse<>( null,"Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "taskresource/get", notes = "taskresource/get")
    @PostMapping("/wms/taskresource/get")
    public ApiResponse<PageInfo<WmsTaskResource>> taskresourceSelectAll(@RequestBody(required = false) WmsTaskResource wmsTaskResource, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        try {
            return new ApiResponse<>(PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> wmsTaskResourceServiceImpl.selectAll(wmsTaskResource)));

        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
    }
}