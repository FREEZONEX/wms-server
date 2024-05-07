package com.supos.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supos.app.entity.WmsRule;
import com.supos.app.mapper.WmsRuleMapper;
import com.supos.app.service.WmsRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class WmsRuleServiceImpl extends ServiceImpl<WmsRuleMapper, WmsRule>
        implements WmsRuleService {

    @Autowired
    private WmsRuleMapper wmsRuleMapper;

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
        return wmsRuleMapper.selectAll(wmsRule);
    }

}




