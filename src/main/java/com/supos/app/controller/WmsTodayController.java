package com.supos.app.controller;

import com.supos.app.config.ApiResponse;
import com.supos.app.entity.WmsInventoryOperation;
import com.supos.app.service.impl.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Api(value = "Today Statistic API", tags = {"08. Statistic"})

@Slf4j
@RestController
public class WmsTodayController {

    @Autowired
    WmsInboundServiceImpl wmsInboundServiceImpl;

    @Autowired
    WmsOutboundServiceImpl wmsOutboundServiceImpl;

    @ApiOperation(value = "wmsllmask",notes = "wmsllmask")
    @PostMapping("wmsllmask")
    public ResponseEntity<String> wmsllmask(@RequestBody Map<String, Object> requestBody) {
        // 在方法内创建RestTemplate实例
        RestTemplate restTemplate = new RestTemplate();
        // 目标URL
        String targetUrl = "http://office.unibutton.com:6589/wmsllmask";
        // 设置HTTP头信息
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        // 创建请求实体对象
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        // 使用RestTemplate将请求转发到目标URL，并接收响应
        ResponseEntity<String> response = restTemplate.exchange(targetUrl, HttpMethod.POST, entity, String.class);
        // 返回响应给客户端
        return response;
    }

    @ApiOperation(value = "today/outbound/done",notes = "today/outbound/done")
    @PostMapping("wms/today/outbound/done")
    public ApiResponse<Map<String, String>> todayOunboundDone() {
        Map<String, String> responseData = new HashMap<>();
        try {
            WmsInventoryOperation wmsInventoryOperation = new WmsInventoryOperation();
            wmsInventoryOperation.setStatus("done");
            List<WmsInventoryOperation> wmsInventoryOperationList = wmsOutboundServiceImpl.selectAll(wmsInventoryOperation);
            if (wmsInventoryOperationList.isEmpty())
                responseData.put("count", "0");
            else
                responseData.put("count", String.valueOf(wmsInventoryOperationList.size()));
            return new ApiResponse<>(responseData);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ApiResponse<>( null,"Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "today/outbound",notes = "today/outbound")
    @PostMapping("wms/today/outbound")
    public ApiResponse<Map<String, String>> todayOubound() {
        Map<String, String> responseData = new HashMap<>();
        try {
            WmsInventoryOperation wmsInventoryOperation = new WmsInventoryOperation();
            List<WmsInventoryOperation> wmsInventoryOperationList = wmsOutboundServiceImpl.selectAll(wmsInventoryOperation);
            if (wmsInventoryOperationList.isEmpty())
                responseData.put("count", "0");
            else
                responseData.put("count", String.valueOf(wmsInventoryOperationList.size()));
            return new ApiResponse<>(responseData);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ApiResponse<>( null,"Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "today/inbound/done",notes = "today/inbound/done")
    @PostMapping("wms/today/inbound/done")
    public ApiResponse<Map<String, String>> todayInboundDone() {
        Map<String, String> responseData = new HashMap<>();
        try {
            WmsInventoryOperation wmsInventoryOperation = new WmsInventoryOperation();
            wmsInventoryOperation.setStatus("done");
            List<WmsInventoryOperation> wmsInventoryOperationList = wmsInboundServiceImpl.selectAll(wmsInventoryOperation);
            if (wmsInventoryOperationList.isEmpty())
                responseData.put("count", "0");
            else
                responseData.put("count", String.valueOf(wmsInventoryOperationList.size()));
            return new ApiResponse<>(responseData);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ApiResponse<>( null,"Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "today/inbound",notes = "today/inbound")
    @PostMapping("wms/today/inbound")
    public ApiResponse<Map<String, String>> todayInbound() {
        Map<String, String> responseData = new HashMap<>();
        try {
            WmsInventoryOperation wmsInventoryOperation = new WmsInventoryOperation();
            List<WmsInventoryOperation> wmsInventoryOperationList = wmsInboundServiceImpl.selectAll(wmsInventoryOperation);
            if (wmsInventoryOperationList.isEmpty())
                responseData.put("count", "0");
            else
                responseData.put("count", String.valueOf(wmsInventoryOperationList.size()));
            return new ApiResponse<>(responseData);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ApiResponse<>( null,"Error occurred while processing the request: " + e.getMessage());
        }
    }
}
