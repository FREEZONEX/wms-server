package com.supos.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supos.app.entity.WmsTask;
import com.supos.app.mapper.WmsTaskMapper;
import com.supos.app.service.WmsTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class WmsTaskServiceImpl extends ServiceImpl<WmsTaskMapper, WmsTask>
        implements WmsTaskService {

    @Autowired
    private WmsTaskMapper wmsTaskMapper;

    public int insertSelective(WmsTask wmsTask) {
        return wmsTaskMapper.insertSelective(wmsTask);
    }

    public int updateTaskById(WmsTask wmsTask) {
        return wmsTaskMapper.updateTaskById(wmsTask);
    }

    public int deleteTaskById(WmsTask wmsTask) {
        return wmsTaskMapper.deleteTaskById(wmsTask);
    }

    public List<WmsTask> selectAll(WmsTask wmsTask) {
        return wmsTaskMapper.selectAll(wmsTask);
    }

}




