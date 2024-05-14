package com.supos.app.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName wms_threed_warehouse
 */
@Data
public class WmsThreedWarehouse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long location_id;
    private String location_name;
    private String material_name;
    private Boolean del_flag;
    private Date update_time;
    private Date create_time;
}