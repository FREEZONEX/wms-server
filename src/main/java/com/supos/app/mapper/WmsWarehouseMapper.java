package com.supos.app.mapper;

import com.supos.app.entity.WmsWarehouse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface WmsWarehouseMapper extends BaseMapper<WmsWarehouse> {
    int insertSelective(WmsWarehouse wmsWarehouse);

    int updateWarehouseById(WmsWarehouse wmsWarehouse);

    int deleteWarehouseById(WmsWarehouse wmsWarehouse);

    List<WmsWarehouse> selectAll(WmsWarehouse wmsWarehouse);
}




