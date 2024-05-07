package com.supos.app.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supos.app.config.ApiResponse;
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
import java.util.stream.IntStream;

@Api(value = "Inbound API", tags = {"05. Inbound"})

@Slf4j
@RestController
public class WmsInboundController {

    @Autowired
    WmsRfidMaterialServiceImpl wmsRfidMaterialServiceImpl;

    @Autowired
    WmsStorageLocationServiceImpl wmsStorageLocationServiceImpl;

    @Autowired
    WmsMaterialServiceImpl wmsMaterialServiceImpl;

    @Autowired
    WmsMaterialTransactionServiceImpl wmsMaterialTransactionServiceImpl;

    @ApiOperation(value = "inbound/add", notes = "inbound/add")
    @PostMapping("/wms/inbound/add")
    public ApiResponse<Map<String, String>> inboundInsert(@RequestBody(required = false) AddInboundRequestNew addInboundRequestNew) {
        try {
            long ID = IdWorker.getId();

            Map<String, String> responseData = new HashMap<>();
            if ("PDA".equals(addInboundRequestNew.getSource())) {

                addInboundRequestNew.getAddInboundRequestDetail().forEach(addInboundRequestDetail -> {

                    WmsMaterialTransaction wmsMaterialTransaction = new WmsMaterialTransaction();
                    wmsMaterialTransaction.setInbound_creator(addInboundRequestNew.getCreator());
                    wmsMaterialTransaction.setInbound_purchase_order_no(addInboundRequestNew.getPurchase_order_no());
                    wmsMaterialTransaction.setInbound_supplier(addInboundRequestNew.getSupplier());
                    wmsMaterialTransaction.setInbound_delivery_date(addInboundRequestNew.getDelivery_date());

                    WmsRfidMaterial wmsRfidMaterial = new WmsRfidMaterial();
                    wmsRfidMaterial.setRf_id(addInboundRequestDetail.getRf_id());
                    wmsMaterialTransaction.setMaterial_code(wmsRfidMaterialServiceImpl.selectall(wmsRfidMaterial).get(0).getMaterial_code());

                    wmsMaterialTransaction.setWarehouse_id(addInboundRequestDetail.getWh_id());

                    //if (addInboundRequestDetail.getWh_id() == 11)
                    {   // update wms_storage_location table
                        WmsStorageLocation wmsStorageLocation = new WmsStorageLocation();
                        wmsStorageLocation.setId(addInboundRequestDetail.getStock_location_id());
                        wmsStorageLocation.setMaterial_name(wmsRfidMaterialServiceImpl.selectall(wmsRfidMaterial).get(0).getMaterial_code());
                        wmsStorageLocationServiceImpl.updateSelectiveByLocationId(wmsStorageLocation);
                    }

                    wmsMaterialTransaction.setStock_location_id(addInboundRequestDetail.getStock_location_id());
                    wmsMaterialTransaction.setInbound_id(ID);
                    wmsMaterialTransaction.setSource(addInboundRequestNew.getSource());
                    wmsMaterialTransaction.setInbound_status("pending");
                    wmsMaterialTransaction.setCreate_time(new Date());

                    IntStream.range(0, addInboundRequestDetail.getQuantity())
                            .forEach(i -> responseData.put("id", String.valueOf(wmsMaterialTransactionServiceImpl.insertSelective(wmsMaterialTransaction))));
                });

            } else if ("manual".equals(addInboundRequestNew.getSource())) {
                addInboundRequestNew.getAddInboundRequestDetail().forEach(addInboundRequestDetail -> {

                    WmsMaterialTransaction wmsMaterialTransaction = new WmsMaterialTransaction();
                    wmsMaterialTransaction.setInbound_creator(addInboundRequestNew.getCreator());
                    wmsMaterialTransaction.setInbound_purchase_order_no(addInboundRequestNew.getPurchase_order_no());
                    wmsMaterialTransaction.setInbound_supplier(addInboundRequestNew.getSupplier());
                    wmsMaterialTransaction.setInbound_delivery_date(addInboundRequestNew.getDelivery_date());
                    wmsMaterialTransaction.setMaterial_code(addInboundRequestDetail.getMaterial_code());
                    wmsMaterialTransaction.setWarehouse_id(addInboundRequestDetail.getWh_id());
                    wmsMaterialTransaction.setStock_location_id(addInboundRequestDetail.getStock_location_id());
                    wmsMaterialTransaction.setInbound_id(ID);
                    wmsMaterialTransaction.setSource(addInboundRequestNew.getSource());
                    wmsMaterialTransaction.setInbound_status("pending");
                    wmsMaterialTransaction.setCreate_time(new Date());

                    //if (addInboundRequestDetail.getWh_id() == 11)
                    {   // update wms_storage_location table
                        WmsStorageLocation wmsStorageLocation = new WmsStorageLocation();
                        wmsStorageLocation.setId(addInboundRequestDetail.getStock_location_id());
                        wmsStorageLocation.setMaterial_name(addInboundRequestDetail.getMaterial_code());
                        wmsStorageLocationServiceImpl.updateSelectiveByLocationId(wmsStorageLocation);
                    }

                    IntStream.range(0, addInboundRequestDetail.getQuantity())
                            .forEach(i -> responseData.put("id", String.valueOf(wmsMaterialTransactionServiceImpl.insertSelective(wmsMaterialTransaction))));
                });
            }
            return new ApiResponse<>(responseData);
        } catch (Exception e) {
            log.info("Error occurred while processing the request: " + e.getMessage(), e); // 使用日志记录异常堆栈
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "inbound/update", notes = "inbound/update")
    @PostMapping("/wms/inbound/update")
    public ApiResponse<Map<String, String>> inboundUpdate(@RequestBody(required = false) UpdateInboundRequest updateInboundRequest) {
        Map<String, String> responseData = new HashMap<>();
        try {
            updateInboundRequest.setUpdate_time(new Date());
            responseData.put("id", String.valueOf(wmsMaterialTransactionServiceImpl.updateByInboundId(updateInboundRequest)));
            return new ApiResponse<>(responseData);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ApiResponse<>( null,"Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "inbound/delete", notes = "inbound/delete")
    @PostMapping("/wms/inbound/delete")
    public ApiResponse<Map<String, String>> inboundDelete(@RequestBody(required = false)ID id) {
        Map<String, String> responseData = new HashMap<>();
        try {
            UpdateInboundRequest updateInboundRequest = new UpdateInboundRequest();
            updateInboundRequest.setId(id.getID());
            responseData.put("id", String.valueOf(wmsMaterialTransactionServiceImpl.deleteForInbound(updateInboundRequest)));
            return new ApiResponse<>(responseData);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ApiResponse<>( null,"Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "inbound/get", notes = "inbound/get")
    @PostMapping("/wms/inbound/get")
    public ApiResponse<PageInfo<InboundDetail>> inboundGet(@RequestBody(required = false) InboundDetail inboundDetail,@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        try {
            return new ApiResponse<>(PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() ->  wmsMaterialTransactionServiceImpl.selectByInboundRfidType(inboundDetail)));
        }catch (Exception e){
            log.info(e.getMessage());
            return new ApiResponse<>( null,"Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "inbound/detail", notes = "inbound/detail")
    @PostMapping("/wms/inbound/detail")
    public ApiResponse<PageInfo<InboundDetail>> inboundDetailGet(@RequestBody(required = false) InboundRecordDetailRequest inboundRecordDetailRequest, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        try {
            WmsMaterialTransaction queryCondition = new WmsMaterialTransaction();
            queryCondition.setRf_id(inboundRecordDetailRequest.getRfid());
            queryCondition.setInbound_id(inboundRecordDetailRequest.getId());

            PageInfo<WmsMaterialTransaction> pageInfo = PageHelper.startPage(pageNum, pageSize)
                    .doSelectPageInfo(() -> wmsMaterialTransactionServiceImpl.selectAllInboundGroupByMaterialCode(queryCondition));

            List<InboundDetail> details = pageInfo.getList().stream().map(transaction -> {
                WmsMaterial wmsMaterial = new WmsMaterial();
                wmsMaterial.setProduct_code(transaction.getMaterial_code());
                WmsMaterial material = wmsMaterialServiceImpl.selectAll(wmsMaterial).get(0);
                return new InboundDetail(transaction, material);
            }).collect(Collectors.toList());

            PageInfo<InboundDetail> resultPageInfo = new PageInfo<>(details);
            BeanUtils.copyProperties(pageInfo, resultPageInfo, "list");
            return new ApiResponse<>(resultPageInfo);
        } catch (Exception e) {
            log.error("Error occurred while processing the request: " + e.getMessage(), e);
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
    }
}