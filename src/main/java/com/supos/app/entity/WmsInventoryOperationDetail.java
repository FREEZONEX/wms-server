package com.supos.app.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class WmsInventoryOperationDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long operation_id;
    private Long location_id;
    private Long material_id;
    private Integer quantity;
    private String rf_id;
    private Date create_time;
    private Date update_time;

    //below 2 properties is only used for stocktaking result output
    private String material_name;
    private Integer stock_quantity;
    private Integer discrepancy;
}
