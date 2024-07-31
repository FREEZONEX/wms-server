package com.supos.app.controller;

import com.supos.app.common.config.ApiResponse;
import com.supos.app.domain.entity.SuposUser;
import com.supos.app.service.SuposUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "SupOS API", tags = {"12. SupOS API"})

@Slf4j
@RestController
public class WmsSupController {

    @Autowired
    SuposUserService suposUserServiceImpl;

    @ApiOperation(value = "user/get", notes = "user/get")
    @PostMapping("/wms/user/get")
    public ApiResponse<List<SuposUser>> getUsers() {
        try {
            return new ApiResponse<>(suposUserServiceImpl.selectAll());
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "user/login", notes = "user/login")
    @PostMapping("/wms/user/login")
    public ApiResponse<SuposUser> login(@RequestBody SuposUser user ) {
        try {
            return new ApiResponse<>(suposUserServiceImpl.login(user.getUsername(), user.getPassword()));
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
    }
}