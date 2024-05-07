package com.supos.app.mapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.supos.app.entity.WmsMaterialExpectedLocation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;


@Mapper
public interface WmsMaterialExpectedLocationMapper extends BaseMapper<WmsMaterialExpectedLocation> {

    int insertAll(List<WmsMaterialExpectedLocation> wmsMaterialExpectedLocations);

    int updateSelective(WmsMaterialExpectedLocation wmsMaterialExpectedLocation);

    int deleteWmsMaterialExpectedLocationById(WmsMaterialExpectedLocation wmsMaterialExpectedLocation);

    int deleteWmsMaterialExpectedLocationByMaterialId(WmsMaterialExpectedLocation wmsMaterialExpectedLocation);

    List<WmsMaterialExpectedLocation> selectAll(WmsMaterialExpectedLocation wmsMaterialExpectedLocation);

    List<WmsMaterialExpectedLocation> selectAvailableLocations(long material_id);

}




