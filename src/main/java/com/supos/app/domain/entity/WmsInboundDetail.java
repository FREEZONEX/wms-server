package com.supos.app.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName wms_inbound_detail
 */
@Data
public class WmsInboundDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long inbound_id;
    private Long location_id;
    private Long material_id;
    private Integer quantity;
    private Date create_time;
    private Date update_time;
}