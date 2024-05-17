package com.supos.app.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName wms_rule
 */
@Data
public class WmsRule implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String task_type;
    private Long warehouse_id;
    private String location_expression;
    private String resource_id_list;
    private String people_name;
    private String note;
    private Boolean enabled;
    private Date create_time;
    private Date update_time;

    // only for response
    private String warehouse_name;
    private String resource_name_list;
}