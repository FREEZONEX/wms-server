package com.supos.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supos.app.domain.entity.WmsResource;
import com.supos.app.domain.entity.WmsRule;
import com.supos.app.domain.entity.WmsWarehouse;
import com.supos.app.mapper.WmsRuleMapper;
import com.supos.app.service.WmsRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class WmsRuleServiceImpl extends ServiceImpl<WmsRuleMapper, WmsRule>
        implements WmsRuleService {

    @Autowired
    private WmsRuleMapper wmsRuleMapper;

    @Autowired
    private WmsWarehouseServiceImpl wmsWarehouseServiceImpl;

    @Autowired
    private WmsResourceServiceImpl wmsResourceServiceImpl;

    public int insertSelective(WmsRule wmsRule) {
        return wmsRuleMapper.insertSelective(wmsRule);
    }

    public int updateRuleById(WmsRule wmsRule) {
        return wmsRuleMapper.updateRuleById(wmsRule);
    }

    public int deleteRuleById(WmsRule wmsRule) {
        return wmsRuleMapper.deleteRuleById(wmsRule);
    }

    public List<WmsRule> selectAll(WmsRule wmsRule) {
        List<WmsRule> wmsRules = wmsRuleMapper.selectAll(wmsRule);
        for (WmsRule rule : wmsRules) {
            WmsWarehouse wmsWarehouse = new WmsWarehouse();
            wmsWarehouse.setId(rule.getWarehouse_id());
            List<WmsWarehouse> wmsWarehouses = wmsWarehouseServiceImpl.selectAll(wmsWarehouse);
            if (!wmsWarehouses.isEmpty()) {
                rule.setWarehouse_name(wmsWarehouses.get(0).getName());
            }
            String resource_id_list = rule.getResource_id_list();
            if (resource_id_list != null) {
                String[] resource_id_list_array = resource_id_list.split(",");
                List<String> resourceNames = new ArrayList<>();
                for (String resource_id : resource_id_list_array) {
                    WmsResource wmsResource = new WmsResource();
                    wmsResource.setId(Long.parseLong(resource_id));
                    List<WmsResource> wmsResources= wmsResourceServiceImpl.selectAll(wmsResource);
                    if (!wmsResources.isEmpty()) {
                        resourceNames.add(wmsResources.get(0).getName());
                    }
                }
                rule.setResource_name_list(String.join(",", resourceNames));
            }
        }
        return wmsRules;
    }

    public List<WmsRule> selectAllMatchRules(WmsRule wmsRule) {
        return wmsRuleMapper.selectAll(wmsRule);
    }

}




