package com.supos.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supos.app.domain.entity.WmsStorageLocation;
import com.supos.app.service.WmsStorageLocationService;
import com.supos.app.mapper.WmsStorageLocationMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;


@Slf4j
@Service
public class WmsStorageLocationServiceImpl extends ServiceImpl<WmsStorageLocationMapper, WmsStorageLocation>
    implements WmsStorageLocationService{

    @Autowired
    private WmsStorageLocationMapper wmsStorageLocationMapper;

    public int insertSelective(WmsStorageLocation wmsStorageLocation) {
        return wmsStorageLocationMapper.insertSelective(wmsStorageLocation);
    }

    public int updateStorageLocationById(WmsStorageLocation wmsStorageLocation) {
        return wmsStorageLocationMapper.updateStorageLocationById(wmsStorageLocation);
    }

    public int deleteStorageLocationById(WmsStorageLocation wmsStorageLocation) {
        return wmsStorageLocationMapper.deleteStorageLocationById(wmsStorageLocation);
    }

    public List<WmsStorageLocation> selectAll(WmsStorageLocation wmsStorageLocation) {
        return wmsStorageLocationMapper.selectAll(wmsStorageLocation);
    }

    public List<WmsStorageLocation> selectAllStocked(int warehouseId) {
        return wmsStorageLocationMapper.selectAllStocked(warehouseId);
    }

    public List<Map<String, Integer>> groupMaterialQuantity(Long warehouse_id) {
        return wmsStorageLocationMapper.groupMaterialQuantity(warehouse_id);
    }
}




