package com.supos.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.supos.app.domain.entity.WmsTaskResource;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface WmsTaskResourceMapper extends BaseMapper<WmsTaskResource> {

    int insertSelective(WmsTaskResource wmsTaskResource);
    int insertAll(List<WmsTaskResource> wmsTaskResources);

    int updateSelective(WmsTaskResource wmsTaskResource);

    int deleteTaskResourceById(WmsTaskResource wmsTaskResource);

    int deleteTaskResourceByTaskId(WmsTaskResource wmsTaskResource);

    List<WmsTaskResource> selectAll(WmsTaskResource wmsTaskResource);
}




