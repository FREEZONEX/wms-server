package com.supos.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supos.app.domain.entity.WmsMaterialExpectedLocation;
import com.supos.app.mapper.WmsMaterialExpectedLocationMapper;
import com.supos.app.service.WmsMaterialExpectedLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WmsMaterialExpectedLocationServiceImpl extends ServiceImpl<WmsMaterialExpectedLocationMapper, WmsMaterialExpectedLocation>
        implements WmsMaterialExpectedLocationService{

    @Autowired
    private WmsMaterialExpectedLocationMapper wmsMaterialExpectedLocationMapper;

    public int insertAll(List<WmsMaterialExpectedLocation> wmsMaterialExpectedLocations) {
        return wmsMaterialExpectedLocationMapper.insertAll(wmsMaterialExpectedLocations);
    }

    public int updateWmsMaterialExpectedLocationById(WmsMaterialExpectedLocation wmsMaterialExpectedLocation) {
        return wmsMaterialExpectedLocationMapper.updateSelective(wmsMaterialExpectedLocation);
    }

    public int deleteWmsMaterialExpectedLocationById(WmsMaterialExpectedLocation wmsMaterialExpectedLocation) {
        return wmsMaterialExpectedLocationMapper.deleteWmsMaterialExpectedLocationById(wmsMaterialExpectedLocation);
    }

    public List<WmsMaterialExpectedLocation> selectAll(WmsMaterialExpectedLocation wmsMaterialExpectedLocation) {
        return wmsMaterialExpectedLocationMapper.selectAll(wmsMaterialExpectedLocation);
    }

}




