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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Api(value = "Material API", tags = {"03. Material"})

@Slf4j
@RestController
public class WmsMaterialController {

    @Autowired
    WmsMaterialServiceImpl wmsMaterialServiceImpl;

    @ApiOperation(value = "material/add",notes = "material/add")
    @PostMapping("/wms/material/add")
    public ApiResponse<Map<String, String>> materialInsert(@RequestBody(required = false) WmsMaterial wmsMaterial) {
        Map<String, String> responseData = new HashMap<>();
        try {
            responseData.put("id", String.valueOf(wmsMaterialServiceImpl.insertSelective(wmsMaterial)));
            return new ApiResponse<>(responseData);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ApiResponse<>( null,"Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "material/update",notes = "material/update")
    @PostMapping("/wms/material/update")
    public ApiResponse<Map<String, String>> materialUpdate(@RequestBody(required = false) WmsMaterial wmsMaterial) {
        Map<String, String> responseData = new HashMap<>();
        try {
            responseData.put("rows_affected", String.valueOf(wmsMaterialServiceImpl.updateMaterialById(wmsMaterial)));
            return new ApiResponse<>(responseData);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ApiResponse<>( null,"Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "material/delete",notes = "material/delete")
    @PostMapping("/wms/material/delete")
    public ApiResponse<Map<String, String>> materialDelete(@RequestBody(required = false) ID id) {
        Map<String, String> responseData = new HashMap<>();
        try {
            WmsMaterial wmsMaterial = new WmsMaterial();
            wmsMaterial.setId(id.getID());
            responseData.put("rows_affected", String.valueOf(wmsMaterialServiceImpl.deleteMaterialById(wmsMaterial)));
            return new ApiResponse<>(responseData);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ApiResponse<>( null,"Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "material/get", notes = "material/get")
    @PostMapping("/wms/material/get")
    public ApiResponse<PageInfo<WmsMaterial>> materialSelectAll(@RequestBody(required = false) WmsMaterial wmsMaterial, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
    //public ApiResponse<PageInfo<MaterialSelectAllResponse>> materialSelectAll(@RequestBody(required = false) WmsMaterial wmsMaterial, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        try {
            return new ApiResponse<>(PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> wmsMaterialServiceImpl.selectAll(wmsMaterial)));

        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
    }
}