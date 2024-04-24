package com.supos.app.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName wms_material_transaction
 */
@Data
public class WmsMaterialTransaction implements Serializable {
    private int quantity;

    /**
     * 
     */
    private Long id;

    /**
     * 
     */
    private String material_code;

    /**
     * 
     */
    private String type;

    /**
     * 
     */
    private String source;

    /**
     * 
     */
    private Long inbound_id;

    /**
     * 
     */
    private Long stocktaking_id;

    /**
     * 
     */
    private Long outbound_id;

    /**
     * 
     */
    private Long warehouse_id;

    /**
     * 
     */
    private Long stock_location_id;

    /**
     * 
     */
    private String rf_id;

    /**
     * 
     */
    private String operator;

    /**
     * 
     */
    private String inbound_status;
    private String outbound_status;


    /**
     * 
     */
    private String note;

    /**
     * 
     */
    private String inbound_creator;

    /**
     * 
     */
    private String outbound_creator;

    /**
     * 
     */
    private String inbound_purchase_order_no;

    /**
     * 
     */
    private String outbound_purchase_order_no;

    /**
     * 
     */
    private String inbound_supplier;

    /**
     * 
     */
    private String outbound_supplier;

    /**
     * 
     */
    private Date inbound_delivery_date;

    /**
     * 
     */
    private Date outbound_delivery_date;
    /**
     * 
     */
    private Integer del_flag;

    /**
     * 
     */
    private Date update_time;

    /**
     * 
     */
    private Date create_time;

    private static final long serialVersionUID = 1L;


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaterial_code() {
        return material_code;
    }

    public void setMaterial_code(String material_code) {
        this.material_code = material_code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getInbound_id() {
        return inbound_id;
    }

    public void setInbound_id(Long inbound_id) {
        this.inbound_id = inbound_id;
    }

    public Long getStocktaking_id() {
        return stocktaking_id;
    }

    public void setStocktaking_id(Long stocktaking_id) {
        this.stocktaking_id = stocktaking_id;
    }

    public Long getOutbound_id() {
        return outbound_id;
    }

    public void setOutbound_id(Long outbound_id) {
        this.outbound_id = outbound_id;
    }

    public Long getWarehouse_id() {
        return warehouse_id;
    }

    public void setWarehouse_id(Long warehouse_id) {
        this.warehouse_id = warehouse_id;
    }

    public Long getStock_location_id() {
        return stock_location_id;
    }

    public void setStock_location_id(Long stock_location_id) {
        this.stock_location_id = stock_location_id;
    }

    public String getRf_id() {
        return rf_id;
    }

    public void setRf_id(String rf_id) {
        this.rf_id = rf_id;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }


    public String getInbound_status() {
        return inbound_status;
    }

    public void setInbound_status(String inbound_status) {
        this.inbound_status = inbound_status;
    }

    public String getOutbound_status() {
        return outbound_status;
    }

    public void setOutbound_status(String outbound_status) {
        this.outbound_status = outbound_status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getInbound_creator() {
        return inbound_creator;
    }

    public void setInbound_creator(String inbound_creator) {
        this.inbound_creator = inbound_creator;
    }

    public String getOutbound_creator() {
        return outbound_creator;
    }

    public void setOutbound_creator(String outbound_creator) {
        this.outbound_creator = outbound_creator;
    }

    public String getInbound_purchase_order_no() {
        return inbound_purchase_order_no;
    }

    public void setInbound_purchase_order_no(String inbound_purchase_order_no) {
        this.inbound_purchase_order_no = inbound_purchase_order_no;
    }

    public String getOutbound_purchase_order_no() {
        return outbound_purchase_order_no;
    }

    public void setOutbound_purchase_order_no(String outbound_purchase_order_no) {
        this.outbound_purchase_order_no = outbound_purchase_order_no;
    }

    public String getInbound_supplier() {
        return inbound_supplier;
    }

    public void setInbound_supplier(String inbound_supplier) {
        this.inbound_supplier = inbound_supplier;
    }

    public String getOutbound_supplier() {
        return outbound_supplier;
    }

    public void setOutbound_supplier(String outbound_supplier) {
        this.outbound_supplier = outbound_supplier;
    }

    public Date getInbound_delivery_date() {
        return inbound_delivery_date;
    }

    public void setInbound_delivery_date(Date inbound_delivery_date) {
        this.inbound_delivery_date = inbound_delivery_date;
    }

    public Date getOutbound_delivery_date() {
        return outbound_delivery_date;
    }

    public void setOutbound_delivery_date(Date outbound_delivery_date) {
        this.outbound_delivery_date = outbound_delivery_date;
    }

    public Integer getDel_flag() {
        return del_flag;
    }

    public void setDel_flag(Integer del_flag) {
        this.del_flag = del_flag;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }
}