package com.supos.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.supos.app.entity.WmsRule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WmsRuleMapper extends BaseMapper<WmsRule> {
    int insertSelective(WmsRule wmsRule);

    int updateRuleById(WmsRule wmsRule);

    int deleteRuleById(WmsRule wmsRule);

    List<WmsRule> selectAll(WmsRule wmsRule);

    List<WmsRule> selectAllMatchRules(WmsRule wmsRule);
}




