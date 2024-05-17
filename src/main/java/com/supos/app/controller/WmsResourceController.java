package com.supos.app.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supos.app.common.config.ApiResponse;
import com.supos.app.domain.entity.WmsResource;
import com.supos.app.service.impl.WmsResourceServiceImpl;
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

@Api(value = "Resource API", tags = {"09. Resource"})
@Slf4j
@RestController
public class WmsResourceController {

    @Autowired
    WmsResourceServiceImpl wmsResourceServiceImpl;

    @ApiOperation(value = "resource/add", notes = "resource/add")
    @PostMapping("/wms/resource/add")
    public ApiResponse<Map<String, String>> resourceInsert(@RequestBody(required = false) WmsResource wmsResource) {
        Map<String, String> responseData = new HashMap<>();
        try {
            responseData.put("rows_affected", String.valueOf(wmsResourceServiceImpl.insertSelective(wmsResource)));
            return new ApiResponse<>(responseData);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "resource/update", notes = "resource/update")
    @PostMapping("/wms/resource/update")
    public ApiResponse<Map<String, String>> resourceUpdate(@RequestBody(required = false) WmsResource wmsResource) {
        Map<String, String> responseData = new HashMap<>();
        try {
            responseData.put("rows_affected", String.valueOf(wmsResourceServiceImpl.updateResourceById(wmsResource)));
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
        return new ApiResponse<>(responseData);
    }

    @ApiOperation(value = "resource/delete", notes = "resource/delete")
    @PostMapping("/wms/resource/delete")
    public ApiResponse<Map<String, String>> resourceDelete(@RequestBody(required = false) WmsResource wmsResource) {
        Map<String, String> responseData = new HashMap<>();
        try {
            responseData.put("rows_affected", String.valueOf(wmsResourceServiceImpl.deleteResourceById(wmsResource)));
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
        return new ApiResponse<>(responseData);
    }

    @ApiOperation(value = "resource/get", notes = "resource/get")
    @PostMapping("/wms/resource/get")
    public ApiResponse<PageInfo<WmsResource>> resourceSelectAll(@RequestBody(required = false) WmsResource wmsResource, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        try {
            PageInfo<WmsResource> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> wmsResourceServiceImpl.selectAll(wmsResource));
            return new ApiResponse<>(pageInfo);
            /*List<WmsResource> wmsResourceList = pageInfo.getList();
            List<ResourceSelectAllResponse> resourceSelectAllResponses = wmsResourceList.stream()
                    .map(resource -> {
                        ResourceSelectAllResponse response = new ResourceSelectAllResponse(resource);
                        return response;
                    })
                    .collect(Collectors.toList());

            PageInfo<ResourceSelectAllResponse> responsePageInfo = new PageInfo<>(resourceSelectAllResponses);
            BeanUtils.copyProperties(pageInfo, responsePageInfo, "list"); // Copy pagination details except the list
            return new ApiResponse<>(responsePageInfo);*/
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
    }

}