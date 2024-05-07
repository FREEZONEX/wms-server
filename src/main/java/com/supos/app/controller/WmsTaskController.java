package com.supos.app.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supos.app.config.ApiResponse;
import com.supos.app.entity.WmsTask;
import com.supos.app.service.impl.WmsTaskServiceImpl;
import com.supos.app.vo.ID;
import com.supos.app.vo.TaskSelectAllResponse;
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

@Api(value = "Task API", tags = {"10. Task"})

@Slf4j
@RestController
public class WmsTaskController {

    @Autowired
    WmsTaskServiceImpl wmsTaskServiceImpl;

    @ApiOperation(value = "task/add",notes = "task/add")
    @PostMapping("/wms/task/add")
    public ApiResponse<Map<String, String>> taskInsert(@RequestBody(required = false) WmsTask wmsTask) {
        Map<String, String> responseData = new HashMap<>();
        try {
            responseData.put("id", String.valueOf(wmsTaskServiceImpl.insertSelective(wmsTask)));
            return new ApiResponse<>(responseData);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ApiResponse<>( null,"Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "task/update",notes = "task/update")
    @PostMapping("/wms/task/update")
    public ApiResponse<Map<String, String>> taskUpdate(@RequestBody(required = false) WmsTask wmsTask) {
        Map<String, String> responseData = new HashMap<>();
        try {
            responseData.put("id", String.valueOf(wmsTaskServiceImpl.updateTaskById(wmsTask)));
            return new ApiResponse<>(responseData);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ApiResponse<>( null,"Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "task/delete",notes = "task/delete")
    @PostMapping("/wms/task/delete")
    public ApiResponse<Map<String, String>> taskDelete(@RequestBody(required = false) ID id) {
        Map<String, String> responseData = new HashMap<>();
        try {
            WmsTask wmsTask = new WmsTask();
            wmsTask.setId(id.getID());
            responseData.put("id", String.valueOf(wmsTaskServiceImpl.deleteTaskById(wmsTask)));
            return new ApiResponse<>(responseData);
        }catch (Exception e){
            log.info(e.getMessage());
            return new ApiResponse<>( null,"Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "task/get", notes = "task/get")
    @PostMapping("/wms/task/get")
    public ApiResponse<PageInfo<TaskSelectAllResponse>> taskSelectAll(@RequestBody(required = false) WmsTask wmsTask, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        try {
            return new ApiResponse<>(PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> wmsTaskServiceImpl.selectAll(wmsTask)));

        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
    }
}