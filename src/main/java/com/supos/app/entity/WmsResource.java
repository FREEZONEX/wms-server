package com.supos.app.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName wms_resource
 */
@Data
public class WmsResource implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String type;
    private String note;
    private Date create_time;
    private Date update_time;

    public WmsResource() {
    }

    // Constructor with id parameter
    public WmsResource(Long id) {
        this.id = id;
    }

}