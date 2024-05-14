package com.supos.app.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * Represents an inbound logistics entity with automated getter/setter,
 * equals, hashCode, and toString methods via Lombok's @Data annotation.
 * @TableName wms_inbound
 */
@Data
public class WmsInbound implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String type;
    private String source;
    private String note;
    private String creator;
    private String operator;
    private String status;
    private String purchase_order_no;
    private String supplier;
    private Date delivery_date;
    private Date create_time;
    private Date update_time;

    public WmsInbound() {
    }

    public WmsInbound(Long id) {
        this.id = id;
    }
}
