package com.supos.app.mapper;

import com.supos.app.domain.entity.WmsStorageLocation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface WmsStorageLocationMapper extends BaseMapper<WmsStorageLocation> {

    int insertSelective(WmsStorageLocation wmsStorageLocation);

    int updateStorageLocationById(WmsStorageLocation wmsStorageLocation);

    int deleteStorageLocationById(WmsStorageLocation wmsStorageLocation);

    List<WmsStorageLocation> selectAll(WmsStorageLocation wmsStorageLocation);

    //int updateSelectiveByLocationId(WmsThreedWarehouse wmsThreedWarehouse);

    List<WmsStorageLocation> selectAllStocked(int warehouse_id);

    List<Map<String, Integer>> groupMaterialQuantity(Long warehouse_id);
}




