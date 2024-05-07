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

@Api(value = "Outbound API", tags = {"06. Outbound"})

@Slf4j
@RestController
public class WmsOutboundController {

    @Autowired
    WmsRfidMaterialServiceImpl wmsRfidMaterialServiceImpl;

    @Autowired
    WmsStorageLocationServiceImpl wmsStorageLocationServiceImpl;

    @Autowired
    WmsMaterialServiceImpl wmsMaterialServiceImpl;

    @Autowired
    WmsMaterialTransactionServiceImpl wmsMaterialTransactionServiceImpl;

    @ApiOperation(value = "outbound/add", notes = "outbound/add")
    @PostMapping("/wms/outbound/add")
    public ApiResponse<Map<String, String>> outboundInsert(@RequestBody(required = false) AddInboundRequestNew addInboundRequestNew) {
        try {
            long ID = IdWorker.getId();
            Map<String, String> responseData = new HashMap<>();

            if ("PDA".equals(addInboundRequestNew.getSource())) {
                addInboundRequestNew.getAddInboundRequestDetail().forEach(addInboundRequestDetail -> {

                    WmsRfidMaterial wmsRfidMaterial = new WmsRfidMaterial();
                    wmsRfidMaterial.setRf_id(addInboundRequestDetail.getRf_id());

                    //if (addInboundRequestDetail.getWh_id() == 11)
                    {   // update wms_storage_location table
                        WmsStorageLocation wmsStorageLocation = new WmsStorageLocation();
                        wmsStorageLocation.setId(addInboundRequestDetail.getStock_location_id());
                        wmsStorageLocation.setMaterial_name("");
                        wmsStorageLocationServiceImpl.updateSelectiveByLocationId(wmsStorageLocation);
                    }

                    int updated = wmsMaterialTransactionServiceImpl.updateForTopNTransactionsOutboundPDA(
                            addInboundRequestNew.getDelivery_date(),
                            addInboundRequestNew.getCreator(),
                            addInboundRequestNew.getPurchase_order_no(),
                            addInboundRequestNew.getSupplier(),
                            null,
                            addInboundRequestNew.getSource(),
                            "pending",
                            addInboundRequestDetail.getRf_id(),
                            ID,
                            addInboundRequestDetail.getStock_location_id(),
                            wmsRfidMaterialServiceImpl.selectall(wmsRfidMaterial).get(0).getMaterial_code(),
                            addInboundRequestDetail.getQuantity()
                    );
                    responseData.put("id", String.valueOf(updated));
                });
            } else if ("manual".equals(addInboundRequestNew.getSource())) {
                addInboundRequestNew.getAddInboundRequestDetail().forEach(addInboundRequestDetail -> {

                    //if (addInboundRequestDetail.getWh_id() == 11)
                    {   // update wms_storage_location table
                        WmsStorageLocation wmsStorageLocation = new WmsStorageLocation();
                        wmsStorageLocation.setId(addInboundRequestDetail.getStock_location_id());
                        wmsStorageLocation.setMaterial_name("");
                        wmsStorageLocationServiceImpl.updateSelectiveByLocationId(wmsStorageLocation);
                    }

                    int updated = wmsMaterialTransactionServiceImpl.updateForTopNTransactionsOutboundManual(
                            addInboundRequestNew.getDelivery_date(),
                            addInboundRequestNew.getCreator(),
                            addInboundRequestNew.getPurchase_order_no(),
                            addInboundRequestNew.getSupplier(),
                            null,
                            addInboundRequestNew.getSource(),
                            "pending",
                            addInboundRequestDetail.getRf_id(),
                            ID,
                            addInboundRequestDetail.getStock_location_id(),
                            addInboundRequestDetail.getMaterial_code(),
                            addInboundRequestDetail.getQuantity()
                    );
                    responseData.put("id", String.valueOf(updated));
                });
            }
            return new ApiResponse<>(responseData);
        } catch (Exception e) {
            log.info("Error occurred while processing the request: " + e.getMessage(), e); // 使用日志记录异常堆栈
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
    }
    @ApiOperation(value = "outbound/update", notes = "outbound/update")
    @PostMapping("/wms/outbound/update")
    public ApiResponse<Map<String, String>> outboundUpdate(@RequestBody(required = false) UpdateInboundRequest updateInboundRequest) {
        Map<String, String> responseData = new HashMap<>();
        try {
            responseData.put("id", String.valueOf(wmsMaterialTransactionServiceImpl.updateByOutboundId(updateInboundRequest)));
            return new ApiResponse<>(responseData);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ApiResponse<>( null,"Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "outbound/delete", notes = "outbound/delete")
    @PostMapping("/wms/outbound/delete")
    public ApiResponse<Map<String, String>> outboundDelete(@RequestBody(required = false) ID id) {
        Map<String, String> responseData = new HashMap<>();
        try {
            UpdateInboundRequest updateInboundRequest = new UpdateInboundRequest();
            updateInboundRequest.setId(id.getID());
            responseData.put("id", String.valueOf(wmsMaterialTransactionServiceImpl.deleteForOutbound(updateInboundRequest)));
            return new ApiResponse<>(responseData);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ApiResponse<>( null,"Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "outbound/get", notes = "outbound/get")
    @PostMapping("/wms/outbound/get")
    public ApiResponse<PageInfo<InboundDetail>> outboundGet(@RequestBody(required = false) InboundDetail inboundDetail,@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        try {
            return new ApiResponse<>(PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() ->  wmsMaterialTransactionServiceImpl.selectByOutboundRfidType(inboundDetail)));
        }catch (Exception e){
            log.info(e.getMessage());
            return new ApiResponse<>( null,"Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "outbound/detail", notes = "outbound/detail")
    @PostMapping("/wms/outbound/detail")
    public ApiResponse<PageInfo<InboundDetail>> outboundDetailGet(@RequestBody(required = false) InboundRecordDetailRequest inboundRecordDetailRequest, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        try {
            WmsMaterialTransaction queryCondition = new WmsMaterialTransaction();
            queryCondition.setRf_id(inboundRecordDetailRequest.getRfid());
            queryCondition.setOutbound_id(inboundRecordDetailRequest.getId());

            PageInfo<WmsMaterialTransaction> pageInfo = PageHelper.startPage(pageNum, pageSize)
                    .doSelectPageInfo(() -> wmsMaterialTransactionServiceImpl.selectAllOutboundGroupByMaterialCodeRfid(queryCondition));

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