package com.supos.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supos.app.domain.entity.*;
import com.supos.app.mapper.WmsTaskMapper;
import com.supos.app.service.WmsTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    private WmsInboundServiceImpl wmsInboundServiceImpl;

    @Autowired
    private WmsOutboundServiceImpl wmsOutboundServiceImpl;

    @Autowired WmsMaterialServiceImpl wmsMaterialServiceImpl;

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
            //1. get resource assigned to this task
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
            // 2. get material name and quantity related to this task
            Map<String, Integer> materialMap = new HashMap<>();
            WmsInventoryOperation wmsInventoryOperation = new WmsInventoryOperation();
            wmsInventoryOperation.setId(task.getOperation_id());
            List<WmsInventoryOperation> wmsInventoryOperations = new ArrayList<>();
            String type = task.getType();
            switch (type) {
                case "putaway":
                    wmsInventoryOperations =  wmsInboundServiceImpl.selectAll(wmsInventoryOperation);
                    break;
                case "pickup":
                    wmsInventoryOperations = wmsOutboundServiceImpl.selectAll(wmsInventoryOperation);
            }
            if (!wmsInventoryOperations.isEmpty()) {
                List<WmsInventoryOperationDetail> details = wmsInventoryOperations.get(0).getWmsInventoryOperationDetails();
                for (WmsInventoryOperationDetail detail : details) {
                    WmsMaterial wmsMaterial = new WmsMaterial();
                    wmsMaterial.setId(detail.getMaterial_id());
                    List<WmsMaterial> wmsMaterials = wmsMaterialServiceImpl.selectAll(wmsMaterial);
                    if (!wmsMaterials.isEmpty()) {
                        String material_name = wmsMaterials.get(0).getName();
                        materialMap.put(material_name, detail.getQuantity());
                    }
                }
                task.setMaterials(materialMap);
            }
        }
        return tasks;
    }

    public int selectCount(WmsTask wmsTask) {
        return wmsTaskMapper.selectCount(wmsTask);
    }
}




