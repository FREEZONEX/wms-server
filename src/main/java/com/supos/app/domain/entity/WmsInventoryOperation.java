package com.supos.app.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class WmsInventoryOperation implements Serializable {

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

    @JsonProperty("details")
    private List<WmsInventoryOperationDetail> wmsInventoryOperationDetails;
}
