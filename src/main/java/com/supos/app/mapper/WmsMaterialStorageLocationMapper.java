package com.supos.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.supos.app.domain.entity.WmsMaterialStorageLocation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface WmsMaterialStorageLocationMapper extends BaseMapper<WmsMaterialStorageLocation> {

    int insertSelective(WmsMaterialStorageLocation wmsMaterialStorageLocation);

    int updateMaterialStorageLocationById(WmsMaterialStorageLocation wmsMaterialStorageLocation);

    int deleteMaterialStorageLocationById(WmsMaterialStorageLocation wmsMaterialStorageLocation);

    List<WmsMaterialStorageLocation> selectAll(WmsMaterialStorageLocation wmsMaterialStorageLocation);

    int GetMaterialQuantity(Long material_id);
}
