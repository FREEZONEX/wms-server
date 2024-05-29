package com.supos.app.service;

import com.supos.app.domain.entity.WmsResourceOccupyLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface WmsResourceOccupyLogService extends IService<WmsResourceOccupyLog> {

    List<WmsResourceOccupyLog> selectResourceOccupyTime(WmsResourceOccupyLog wmsResourceOccupyLog);
    Map<String, Object> selectUtilization(WmsResourceOccupyLog wmsResourceOccupyLog,int periodDays);
}
