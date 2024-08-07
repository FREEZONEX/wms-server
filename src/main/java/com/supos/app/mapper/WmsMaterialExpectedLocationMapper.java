package com.supos.app.mapper;
import org.apache.ibatis.annotations.Mapper;

import com.supos.app.domain.entity.WmsMaterialExpectedLocation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;


@Mapper
public interface WmsMaterialExpectedLocationMapper extends BaseMapper<WmsMaterialExpectedLocation> {

    int insertAll(List<WmsMaterialExpectedLocation> wmsMaterialExpectedLocations);

    int updateSelective(WmsMaterialExpectedLocation wmsMaterialExpectedLocation);

    int deleteWmsMaterialExpectedLocationById(WmsMaterialExpectedLocation wmsMaterialExpectedLocation);

    int deleteWmsMaterialExpectedLocationByMaterialId(WmsMaterialExpectedLocation wmsMaterialExpectedLocation);

    List<WmsMaterialExpectedLocation> selectAll(WmsMaterialExpectedLocation wmsMaterialExpectedLocation);

    List<WmsMaterialExpectedLocation> selectAvailableLocations(long material_id, String exclude_location_ids);      // exclude_location_ids is comma separated string

    List<WmsMaterialExpectedLocation> selectAvailableLocationsForOutbound(long material_id, String exclude_location_ids); //avalibale locations of exsiting material

}




