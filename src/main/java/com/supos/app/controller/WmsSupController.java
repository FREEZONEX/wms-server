package com.supos.app.controller;

import com.supos.app.config.ApiResponse;
import com.supos.app.entity.SysUser;
import com.supos.app.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "SupOS API", tags = {"12. SupOS API"})

@Slf4j
@RestController
public class WmsSupController {

    @Autowired
    SysUserService sysUserServiceImpl;

    @ApiOperation(value = "user/get", notes = "user/get")
    @PostMapping("/wms/user/get")
    public ApiResponse<List<SysUser>> getUsers() {
        try {
            return new ApiResponse<>(sysUserServiceImpl.queryAll());
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
    }

}