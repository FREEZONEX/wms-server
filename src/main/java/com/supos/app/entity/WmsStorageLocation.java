package com.supos.app.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName wms_storage_location
 */
@Data
public class WmsStorageLocation implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long warehouse_id;
    private String name;
    private String material_name;
    private Integer quantity;
    private Date update_time;
    private Date create_time;
}