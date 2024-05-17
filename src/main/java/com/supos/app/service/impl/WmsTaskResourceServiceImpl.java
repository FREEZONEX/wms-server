package com.supos.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supos.app.domain.entity.WmsTaskResource;
import com.supos.app.mapper.WmsTaskResourceMapper;
import com.supos.app.service.WmsTaskResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class WmsTaskResourceServiceImpl extends ServiceImpl<WmsTaskResourceMapper, WmsTaskResource>
        implements WmsTaskResourceService {

    @Autowired
    private WmsTaskResourceMapper wmsTaskResourceMapper;

    public int insertSelective(WmsTaskResource wmsTaskResource, boolean ignoreDuplicate, boolean triggerResourceOccupyLog) {
        if (ignoreDuplicate) {
            List<WmsTaskResource> items = selectAll(wmsTaskResource);
            if (!items.isEmpty())
                return 0;
        }

        int rows_affected = wmsTaskResourceMapper.insertSelective(wmsTaskResource);

        if (rows_affected > 0 && triggerResourceOccupyLog) {
            // add resource occupy log
        }

        return rows_affected;
    }

    public int updateSelective(WmsTaskResource wmsTaskResource) {
        return wmsTaskResourceMapper.updateSelective(wmsTaskResource);
    }

    public int deleteTaskResourceById(WmsTaskResource wmsTaskResource) {
        return wmsTaskResourceMapper.deleteTaskResourceById(wmsTaskResource);
    }

    public List<WmsTaskResource> selectAll(WmsTaskResource wmsTaskResource) {
        return wmsTaskResourceMapper.selectAll(wmsTaskResource);
    }

}




