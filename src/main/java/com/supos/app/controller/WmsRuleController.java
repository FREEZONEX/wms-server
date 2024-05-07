package com.supos.app.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supos.app.config.ApiResponse;
import com.supos.app.entity.WmsRule;
import com.supos.app.service.impl.WmsRuleServiceImpl;
import com.supos.app.vo.ID;
import com.supos.app.vo.RuleSelectAllResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api(value = "Rule API", tags = {"11. Rule"})

@Slf4j
@RestController
public class WmsRuleController {

    @Autowired
    WmsRuleServiceImpl wmsRuleServiceImpl;

    @ApiOperation(value = "rule/add", notes = "rule/add")
    @PostMapping("/wms/rule/add")
    public ApiResponse<Map<String, String>> ruleInsert(@RequestBody(required = false) WmsRule wmsRule) {
        Map<String, String> responseData = new HashMap<>();
        try {
            responseData.put("id", String.valueOf(wmsRuleServiceImpl.insertSelective(wmsRule)));
            return new ApiResponse<>(responseData);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
    }

    @ApiOperation(value = "rule/update", notes = "rule/update")
    @PostMapping("/wms/rule/update")
    public ApiResponse<Map<String, String>> ruleUpdate(@RequestBody(required = false) WmsRule wmsRule) {
        Map<String, String> responseData = new HashMap<>();
        try {
            responseData.put("id", String.valueOf(wmsRuleServiceImpl.updateRuleById(wmsRule)));
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
        return new ApiResponse<>(responseData);
    }

    @ApiOperation(value = "rule/delete", notes = "rule/delete")
    @PostMapping("/wms/rule/delete")
    public ApiResponse<Map<String, String>> ruleDelete(@RequestBody(required = false) ID id) {
        Map<String, String> responseData = new HashMap<>();
        try {
            WmsRule wmsRule = new WmsRule();
            wmsRule.setId(id.getID());
            responseData.put("id", String.valueOf(wmsRuleServiceImpl.deleteRuleById(wmsRule)));
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
        return new ApiResponse<>(responseData);
    }

    @ApiOperation(value = "rule/get", notes = "rule/get")
    @PostMapping("/wms/rule/get")
    public ApiResponse<PageInfo<RuleSelectAllResponse>> ruleSelectAll(@RequestBody(required = false) WmsRule wmsRule, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        try {
            PageInfo<WmsRule> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> wmsRuleServiceImpl.selectAll(wmsRule));

            List<WmsRule> wmsRuleList = pageInfo.getList();
            List<RuleSelectAllResponse> ruleSelectAllResponses = wmsRuleList.stream()
                    .map(rule -> {
                        RuleSelectAllResponse response = new RuleSelectAllResponse(rule);
                        return response;
                    })
                    .collect(Collectors.toList());

            PageInfo<RuleSelectAllResponse> responsePageInfo = new PageInfo<>();
            //BeanUtils.copyProperties(pageInfo, responsePageInfo, "list"); // Copy pagination details except the list
            return new ApiResponse<>(responsePageInfo);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ApiResponse<>(null, "Error occurred while processing the request: " + e.getMessage());
        }
    }

}