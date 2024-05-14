package com.supos.app.mapper;

import com.supos.app.entity.WmsStorageLocation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.supos.app.entity.WmsThreedWarehouse;
import com.supos.app.entity.WmsWarehouse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface WmsStorageLocationMapper extends BaseMapper<WmsStorageLocation> {

    int insertSelective(WmsStorageLocation wmsStorageLocation);

    int updateStorageLocationById(WmsStorageLocation wmsStorageLocation);

    int deleteStorageLocationById(WmsStorageLocation wmsStorageLocation);

    List<WmsStorageLocation> selectAll(WmsStorageLocation wmsStorageLocation);

    //int updateSelectiveByLocationId(WmsThreedWarehouse wmsThreedWarehouse);

    List<WmsStorageLocation> selectAllStocked(int warehouse_id);
}




