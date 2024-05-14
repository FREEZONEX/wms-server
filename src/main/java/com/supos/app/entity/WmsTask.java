package com.supos.app.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 
 * @TableName wms_resource
 */
@Data
public class WmsTask implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long operation_id;
    private String type;
    private String note;
    private String status;
    private String people_name;
    private Date create_time;
    private Date update_time;

    private Map<Long, String> resources;
}