package com.supos.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supos.app.domain.entity.WmsResourceOccupyLog;
import com.supos.app.domain.vo.ResourceUsageModel;
import com.supos.app.service.WmsResourceOccupyLogService;
import com.supos.app.mapper.WmsResourceOccupyLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class WmsResourceOccupyLogServiceImpl extends ServiceImpl<WmsResourceOccupyLogMapper, WmsResourceOccupyLog>
    implements WmsResourceOccupyLogService{

    @Autowired
    private WmsResourceOccupyLogMapper wmsResourceOccupyLogMapper;

    public int insertSelective(WmsResourceOccupyLog wmsResourceOccupyLog) {
        return wmsResourceOccupyLogMapper.insert(wmsResourceOccupyLog);
    }

    public List<WmsResourceOccupyLog> selectResourceOccupyTime(WmsResourceOccupyLog wmsResourceOccupyLog) {
        return wmsResourceOccupyLogMapper.selectResourceOccupyTime(wmsResourceOccupyLog);
    }

    public Map<String, Object> selectUtilization(WmsResourceOccupyLog wmsResourceOccupyLog,int periodDays) {
        Map<String, Object> responseData = new HashMap<>();

        List<WmsResourceOccupyLog> resourceOccupyList=wmsResourceOccupyLogMapper.selectResourceOccupyTime(wmsResourceOccupyLog);
        List<ResourceUsageModel> occupyResult=new ArrayList<>();
        List<ResourceUsageModel> idleResult=new ArrayList<>();
        long occupyTotal=0;
        for(WmsResourceOccupyLog o:resourceOccupyList){
            //usage
            ResourceUsageModel model=new ResourceUsageModel();
            model.setName(o.getResource_name());
            long diffSeconds=0;
            if(o.getTotal_time_diff_seconds()!=null)
            {
                diffSeconds=o.getTotal_time_diff_seconds();
                model.setOccupyTotalTime(this.getOccupyTime(diffSeconds));
                model.setOccupyRate(this.getOccupyTimeRateByDays(diffSeconds,periodDays));
                model.setOccupyMaxRate(maxRate);
                occupyResult.add(model);
                occupyTotal+=diffSeconds;
            }
            //idle
            if(o.getTotal_time_diff_seconds()==null)
            {
                diffSeconds=(long)periodDays*24*60*60;
            }
            model.setIdleTotalTime(this.getIdleTimeByDay(diffSeconds,periodDays));
            idleResult.add(model);
        }

        int pastOccupyTaskCount=wmsResourceOccupyLogMapper.selectTaskCountInRange(wmsResourceOccupyLog);
        long average=occupyTotal/pastOccupyTaskCount;
        responseData.put("averageTime",getOccupyTime(average));
        responseData.put("occupy",occupyResult);
        responseData.put("idle",idleResult);
        return responseData;
    }

    private String getOccupyTime(long diffInSeconds) {
        return getTimeText(diffInSeconds);
    }

    private static final int maxRate=100;
    private String getIdleTimeByDay(long diffInSeconds,int periodDays) {
        long diff=(long)periodDays*24*60*60-diffInSeconds;
        return diff==0?getTimeText(diffInSeconds):getTimeText(diff);
    }

    private String getTimeText(long diffInSeconds) {
        int hours = (int) (diffInSeconds / 3600);
        int minutes = (int) ((diffInSeconds % 3600) / 60);
        int seconds = (int) (diffInSeconds % 60);

        // use DecimalFormat to ensure minutes and seconds are two digits
        DecimalFormat df = new DecimalFormat("00");
        String formattedMinutes = df.format(minutes);
        String formattedSeconds = df.format(seconds);

        return hours + "h" + formattedMinutes + "m" + formattedSeconds + "s";
    }

    private float getOccupyTimeRateByDays(long diffInSeconds,int periodDays) {
        return (float)(diffInSeconds/(long)periodDays*24*60*60)*100;
    }

}




