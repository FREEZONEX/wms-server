package com.supos.app.vo;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.supos.app.entity.WmsInventoryOperationDetail;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class AddInboundRequestNew {
    private String type;
    private String source;
    private String note;
    private String creator;
    private String operator;
    private String status;
    private String purchase_order_no;
    private String supplier;
    private Date delivery_date;

    @JsonProperty("request_detail")
    private List<WmsInventoryOperationDetail> addInboundRequestDetail;
}
