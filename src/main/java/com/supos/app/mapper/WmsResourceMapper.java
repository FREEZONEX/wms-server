package com.supos.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.supos.app.entity.WmsResource;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WmsResourceMapper extends BaseMapper<WmsResource> {
    int insertSelective(WmsResource wmsResource);

    int updateResourceById(WmsResource wmsResource);

    int deleteResourceById(WmsResource wmsResource);

    List<WmsResource> selectAll(WmsResource wmsResource);
}




