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

@Api(value = "RFID API", tags = {"04. RFID"})

@Slf4j
@RestController
public class WmsRfidmaterialController {

    @Autowired
    WmsRfidMaterialServiceImpl wmsRfidMaterialServiceImpl;

    @ApiOperation(value = "rfidmaterial/add",notes = "rfidmaterial/add")
    @PostMapping("/wms/rfidmaterial/add")
    public ApiResponse<Map<String, String>> rfidmaterialInsert(@RequestBody(required = false) WmsRfidMaterial wmsRfidMaterial) {
        Map<String, String> responseData = new HashMap<>();
        try {
            responseData.put("rows_affected", String.valueOf(wmsRfidMaterialServiceImpl.insertSelective(wmsRfidMaterial)));
            return new ApiResponse<>(responseData);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ApiResponse<>( null,"Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "rfidmaterial/update", notes = "rfidmaterial/update")
    @PostMapping("/wms/rfidmaterial/update")
    public ApiResponse<Map<String, String>> rfidmaterialUpdate(@RequestBody(required = false) WmsRfidMaterial wmsRfidMaterial) {
        Map<String, String> responseData = new HashMap<>();
        try {
            responseData.put("rows_affected",String.valueOf(wmsRfidMaterialServiceImpl.updateSelective(wmsRfidMaterial)));
            return new ApiResponse<>(responseData);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "rfidmaterial/delete",notes = "rfidmaterial/delete")
    @PostMapping("/wms/rfidmaterial/delete")
    public ApiResponse<Map<String, String>> rfidmaterialDelete(@RequestBody(required = false) ID id) {
        Map<String, String> responseData = new HashMap<>();
        try {
            WmsRfidMaterial wmsRfidMaterial = new WmsRfidMaterial();
            wmsRfidMaterial.setId(id.getID());
            responseData.put("rows_affected", String.valueOf(wmsRfidMaterialServiceImpl.deleteSelective(wmsRfidMaterial)));
            return new ApiResponse<>(responseData);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ApiResponse<>( null,"Error occurred while processing the request: " + e.getMessage());
        }
    }
    @ApiOperation(value = "rfidmaterial/get", notes = "rfidmaterial/get")
    @PostMapping("/wms/rfidmaterial/get")
    public ApiResponse<PageInfo<WmsRfidMaterial>> rfidmaterialGet(@RequestBody(required = false) WmsRfidMaterial wmsRfidMaterial,@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        try {
            return new ApiResponse<>(PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> wmsRfidMaterialServiceImpl.selectall(wmsRfidMaterial)));
        } catch (Exception e) {
            log.info("Error occurred while processing the request: " + e.getMessage(), e);
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
    }

}