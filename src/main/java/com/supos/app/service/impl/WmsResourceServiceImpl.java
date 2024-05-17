package com.supos.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supos.app.domain.entity.WmsResource;
import com.supos.app.mapper.WmsResourceMapper;
import com.supos.app.service.WmsResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class WmsResourceServiceImpl extends ServiceImpl<WmsResourceMapper, WmsResource>
        implements WmsResourceService {

    @Autowired
    private WmsResourceMapper wmsResourceMapper;

    public int insertSelective(WmsResource wmsResource) {
        return wmsResourceMapper.insertSelective(wmsResource);
    }

    public int updateResourceById(WmsResource wmsResource) {
        return wmsResourceMapper.updateResourceById(wmsResource);
    }

    public int deleteResourceById(WmsResource wmsResource) {
        return wmsResourceMapper.deleteResourceById(wmsResource);
    }

    public List<WmsResource> selectAll(WmsResource wmsResource) {
        return wmsResourceMapper.selectAll(wmsResource);
    }

}




