package com.supos.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supos.app.entity.WmsResource;
import com.supos.app.entity.WmsTask;
import com.supos.app.entity.WmsTaskResource;
import com.supos.app.mapper.WmsTaskMapper;
import com.supos.app.service.WmsTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class WmsTaskServiceImpl extends ServiceImpl<WmsTaskMapper, WmsTask>
        implements WmsTaskService {

    @Autowired
    private WmsTaskMapper wmsTaskMapper;

    @Autowired
    private WmsTaskResourceServiceImpl wmsTaskResourceServiceImpl;

    @Autowired
    private WmsResourceServiceImpl wmsResourceServiceImpl;

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
        List<WmsTask> tasks = wmsTaskMapper.selectAll(wmsTask);
        for (WmsTask task : tasks) {
            Map<Long, String> resourceMap = new HashMap<>();
            WmsTaskResource wmsTaskResource = new WmsTaskResource();
            wmsTaskResource.setTask_id(task.getId());
            List<WmsTaskResource> taskResources = wmsTaskResourceServiceImpl.selectAll(wmsTaskResource);
            if (!taskResources.isEmpty()) {
                for (WmsTaskResource taskResource : taskResources) {
                    Long resource_id = taskResource.getResource_id();
                    WmsResource wmsResource = new WmsResource();
                    List<WmsResource> resources = wmsResourceServiceImpl.selectAll(wmsResource);
                    if (!resources.isEmpty()) {
                        String resource_name = resources.get(0).getName();
                        resourceMap.put(resource_id, resource_name);
                    }
                }
                task.setResources(resourceMap);
            }
        }
        return tasks;
    }

}




