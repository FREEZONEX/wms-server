package com.supos.app.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @TableName wms_warehouse
 */
@Data
public class WmsWarehouse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String warehouse_id;
    private String name;
    private String type;
    private String manager;
    private String department;
    private String email;
    private String project_group;
    private String note;
    private Date create_time;
    private Date update_time;
}