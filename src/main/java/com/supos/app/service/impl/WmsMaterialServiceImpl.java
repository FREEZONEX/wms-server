package com.supos.app.service.impl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supos.app.domain.entity.WmsMaterial;
import com.supos.app.domain.entity.WmsMaterialExpectedLocation;
import com.supos.app.domain.entity.WmsMaterialStorageLocation;
import com.supos.app.domain.entity.WmsStorageLocation;
import com.supos.app.domain.entity.WmsWarehouse;
import com.supos.app.mapper.WmsMaterialExpectedLocationMapper;
import com.supos.app.mapper.WmsMaterialStorageLocationMapper;
import com.supos.app.service.WmsMaterialService;
import com.supos.app.mapper.WmsMaterialMapper;
import com.supos.app.mapper.WmsWarehouseMapper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class WmsMaterialServiceImpl extends ServiceImpl<WmsMaterialMapper, WmsMaterial>
        implements WmsMaterialService{

    @Autowired
    private WmsMaterialMapper wmsMaterialMapper;

    @Autowired
    private WmsMaterialExpectedLocationMapper wmsMaterialExpectedLocationMapper;

    @Autowired
    private WmsWarehouseMapper wmsWarehouseMapper;

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

    public Long importMaterialCSV(MultipartFile file) {
        Long count = 0L;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true) // Skip the first record as it is the header
                .build())) {
/*
    private Long id;
    private String material_code;
    private String name;
    private String material_type;
    private String unit;
    private String note;
    private String specification;
    private Long max;
    private Long min;
    private Long quantity;
    private String status;
    private Long expect_wh_id;
    private String expect_storage_locations;
 */
            for (CSVRecord record : csvParser) {
                WmsMaterial wmsMaterial = new WmsMaterial();
                Long material_id = Long.parseLong(record.get("id"));
                wmsMaterial.setId(material_id);
                wmsMaterial.setMaterial_code(record.get("material_code"));
                wmsMaterial.setName(record.get("name"));
                wmsMaterial.setMaterial_type(record.get("material_type"));
                wmsMaterial.setUnit(record.get("unit"));
                wmsMaterial.setNote(record.get("note"));
                wmsMaterial.setSpecification(record.get("specification"));
                wmsMaterial.setMax(Long.parseLong(record.get("max")));
                wmsMaterial.setMin(Long.parseLong(record.get("min")));
                wmsMaterial.setQuantity(Long.parseLong(record.get("quantity")));
                wmsMaterial.setStatus(record.get("status"));
                
                // add material to db first
                wmsMaterialMapper.insertSelective(wmsMaterial);
                count++;

                // then add expected locations to db
                String expect_warehouse = record.get("expect_warehouse");
                if (expect_warehouse != null && !expect_warehouse.isEmpty()) {
                    WmsWarehouse wmsWarehouse = new WmsWarehouse();
                    wmsWarehouse.setName(expect_warehouse);
                    List<WmsWarehouse> wmsWarehouseList = wmsWarehouseMapper.selectAll(wmsWarehouse);
                    if (wmsWarehouseList != null &&!wmsWarehouseList.isEmpty()) {
                        Long warehouse_id = wmsWarehouseList.get(0).getId();

                        String expect_storage_locations = record.get("expect_storage_locations");
                        if(expect_storage_locations != null && !expect_storage_locations.isEmpty()) {
                            List<String> location_names = Arrays.stream(expect_storage_locations.split("/"))
                                    .map(String::trim)
                                    .collect(Collectors.toList());
                            
                            List<WmsMaterialExpectedLocation> wmsMaterialExpectedLocations = new ArrayList<>();
                            for(String location_name : location_names) {
                                WmsStorageLocation wmsStorageLocation = new WmsStorageLocation();
                                wmsStorageLocation.setWarehouse_id(warehouse_id);
                                wmsStorageLocation.setName(location_name);
                                List<WmsStorageLocation> locationList = wmsStorageLocationServiceImpl.selectAll(wmsStorageLocation);
                                if (locationList != null && !locationList.isEmpty()) {
                                    long locationId = locationList.get(0).getId();
                                    WmsMaterialExpectedLocation wmsMaterialExpectedLocation = new WmsMaterialExpectedLocation();
                                    wmsMaterialExpectedLocation.setMaterial_id(material_id);
                                    wmsMaterialExpectedLocation.setLocation_id(locationId);
                                    wmsMaterialExpectedLocations.add(wmsMaterialExpectedLocation);
                                }
                            }
                            
                            wmsMaterialExpectedLocationMapper.insertAll(wmsMaterialExpectedLocations);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("CSV import fail: {}", e.getMessage(), e);
            throw new RuntimeException("CSV import fail：" + e.getMessage());
        }

        return count;
    }
}




