package com.supos.app.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supos.app.domain.entity.WmsMaterial;
import com.supos.app.domain.entity.WmsMaterialExpectedLocation;
import com.supos.app.domain.entity.WmsMaterialStorageLocation;
import com.supos.app.domain.entity.WmsStorageLocation;
import com.supos.app.mapper.WmsMaterialExpectedLocationMapper;
import com.supos.app.mapper.WmsMaterialStorageLocationMapper;
import com.supos.app.service.WmsMaterialService;
import com.supos.app.mapper.WmsMaterialMapper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class WmsMaterialServiceImpl extends ServiceImpl<WmsMaterialMapper, WmsMaterial>
        implements WmsMaterialService{

    @Autowired
    private WmsMaterialMapper wmsMaterialMapper;

    @Autowired
    private WmsMaterialExpectedLocationMapper wmsMaterialExpectedLocationMapper;

    @Autowired
    private WmsMaterialStorageLocationMapper wmsMaterialStorageLocationMapper;

    @Autowired
    private  WmsStorageLocationServiceImpl wmsStorageLocationServiceImpl;

    public long insertSelective(WmsMaterial wmsMaterial) {
        if (wmsMaterial.getId() == null) {
            //long material_id = CommonUtils.generateUnsignedLongUUID();
            long material_id = IdWorker.getId();
            wmsMaterial.setId(material_id);
        }
        wmsMaterialMapper.insertSelective(wmsMaterial);
        long warehouse_id = wmsMaterial.getExpect_wh_id();
        //String[] locations=wmsMaterial.getLocations();
        String expect_storage_locations = wmsMaterial.getExpect_storage_locations();
        if(expect_storage_locations != null && !expect_storage_locations.isEmpty()){
            String[] locations = expect_storage_locations.split(",");
            //wmsMaterial.setExpect_storage_locations(joinedLocations);
            List<WmsMaterialExpectedLocation> wmsMaterialExpectedLocations = ConstructLocationsFromPlane(warehouse_id, locations, wmsMaterial.getId());
            if (!wmsMaterialExpectedLocations.isEmpty())
                wmsMaterialExpectedLocationMapper.insertAll(wmsMaterialExpectedLocations);
        }
        return wmsMaterial.getId();
    }

    @NotNull
    private List<WmsMaterialExpectedLocation> ConstructLocationsFromPlane(long warehouse_id, String[] locations, long material_id) {
        List<WmsMaterialExpectedLocation> wmsMaterialExpectedLocations = new ArrayList<>();
        for(String location : locations){
            // Filling locations
            for (int i = 0; i < 9; i++) {
                for (int j = 1; j < 6; j++) {
                    String shelfLocation = location + "-" + (char)('A' + i) + j;
                    WmsStorageLocation wmsStorageLocation = new WmsStorageLocation();
                    wmsStorageLocation.setWarehouse_id(warehouse_id);
                    wmsStorageLocation.setName(shelfLocation);
                    List<WmsStorageLocation> locationList = wmsStorageLocationServiceImpl.selectAll(wmsStorageLocation);
                    if (locationList != null && !locationList.isEmpty()) {
                        long locationId = locationList.get(0).getId();
                        WmsMaterialExpectedLocation wmsMaterialExpectedLocation = new WmsMaterialExpectedLocation();
                        wmsMaterialExpectedLocation.setMaterial_id(material_id);
                        wmsMaterialExpectedLocation.setLocation_id(locationId);
                        wmsMaterialExpectedLocations.add(wmsMaterialExpectedLocation);
                    }
                }
            }
        }
        return wmsMaterialExpectedLocations;
    }

    public int updateMaterialById(WmsMaterial wmsMaterial) {
        // update sub table first
        WmsMaterialExpectedLocation wmsMaterialExpectedLocation = new WmsMaterialExpectedLocation();
        wmsMaterialExpectedLocation.setMaterial_id(wmsMaterial.getId());
        wmsMaterialExpectedLocationMapper.deleteWmsMaterialExpectedLocationByMaterialId(wmsMaterialExpectedLocation);

        long warehouse_id = wmsMaterial.getExpect_wh_id();
        String expect_storage_locations = wmsMaterial.getExpect_storage_locations();
        if(expect_storage_locations != null && !expect_storage_locations.isEmpty()){
            String[] locations = expect_storage_locations.split(",");
            List<WmsMaterialExpectedLocation> wmsMaterialExpectedLocations = ConstructLocationsFromPlane(warehouse_id, locations, wmsMaterial.getId());
            wmsMaterialExpectedLocationMapper.insertAll(wmsMaterialExpectedLocations);
        }

        return wmsMaterialMapper.updateSelective(wmsMaterial);
    }

    public int deleteMaterialById(WmsMaterial wmsMaterial) {
        WmsMaterialExpectedLocation wmsMaterialExpectedLocation = new WmsMaterialExpectedLocation();
        wmsMaterialExpectedLocation.setMaterial_id(wmsMaterial.getId());
        wmsMaterialExpectedLocationMapper.deleteWmsMaterialExpectedLocationByMaterialId(wmsMaterialExpectedLocation);
;       return wmsMaterialMapper.deleteWmsMaterialById(wmsMaterial);
    }

    public List<WmsMaterial> selectAll(WmsMaterial wmsMaterial) {
        List<WmsMaterial> wmsMaterialList = wmsMaterialMapper.selectAll(wmsMaterial);
        List<String> excludeLocationIdsList = new ArrayList<>();

        for(WmsMaterial material: wmsMaterialList) {
            // 1. calculate suggested storage location id
            List<WmsMaterialExpectedLocation> availableLocations = wmsMaterialExpectedLocationMapper.selectAvailableLocations(material.getId(), String.join(",", excludeLocationIdsList));
            if(!availableLocations.isEmpty()) {
                Long location_id = availableLocations.get(0).getLocation_id();
                material.setSuggested_storage_location_id(location_id);
                WmsStorageLocation wmsStorageLocation = new WmsStorageLocation();
                wmsStorageLocation.setId(location_id);
                List<WmsStorageLocation> locationList = wmsStorageLocationServiceImpl.selectAll(wmsStorageLocation);
                if (locationList != null && !locationList.isEmpty()) {
                    material.setSuggested_storage_location_name(locationList.get(0).getName());
                    excludeLocationIdsList.add(String.valueOf(locationList.get(0).getId()));
                }
            }

            // 2. fill exist stocks for this material
            if(Boolean.TRUE.equals(wmsMaterial.getShow_stock())) {
                WmsMaterialStorageLocation wmsMaterialStorageLocation = new WmsMaterialStorageLocation();
                wmsMaterialStorageLocation.setMaterial_id(wmsMaterial.getId());
                List<WmsMaterialStorageLocation> wmsMaterialStorageLocations = wmsMaterialStorageLocationMapper.selectAll(wmsMaterialStorageLocation);
                material.setWmsMaterialStorageLocations(wmsMaterialStorageLocations);
            }
        }

        return wmsMaterialList;
    }
}




