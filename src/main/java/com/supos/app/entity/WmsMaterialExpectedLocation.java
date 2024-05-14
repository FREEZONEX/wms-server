package com.supos.app.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @TableName wms_material_expected_location
 */
@Data
public class WmsMaterialExpectedLocation implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long material_id;
    private Long location_id;
    private Date create_time;
    private Date update_time;
}