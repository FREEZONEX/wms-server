package com.supos.app.mapper;

import com.supos.app.domain.entity.WmsResourceOccupyLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WmsResourceOccupyLogMapper extends BaseMapper<WmsResourceOccupyLog> {

    List<WmsResourceOccupyLog> selectResourceOccupyTime(WmsResourceOccupyLog wmsResourceOccupyLog);
    int selectTaskCountInRange(WmsResourceOccupyLog wmsResourceOccupyLog);
}




