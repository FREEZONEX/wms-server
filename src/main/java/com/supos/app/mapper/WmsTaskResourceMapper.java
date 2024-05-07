package com.supos.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.supos.app.entity.WmsTaskResource;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface WmsTaskResourceMapper extends BaseMapper<WmsTaskResource> {

    int insertAll(List<WmsTaskResource> wmsTaskResources);

    int updateSelective(WmsTaskResource wmsTaskResource);

    int deleteWmsTaskResourceById(WmsTaskResource wmsTaskResource);

    int deleteWmsTaskResourceByTaskId(WmsTaskResource wmsTaskResource);

    List<WmsTaskResource> selectAll(WmsTaskResource wmsTaskResource);
}




