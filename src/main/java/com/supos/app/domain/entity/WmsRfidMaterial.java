package com.supos.app.domain.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName wms_rfid_material
 */
@Data
public class WmsRfidMaterial implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String rf_id;
    private Long material_id;
    private Date update_time;
    private Date create_time;
}