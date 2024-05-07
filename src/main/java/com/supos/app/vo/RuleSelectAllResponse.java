package com.supos.app.vo;

import com.supos.app.entity.WmsRule;

import java.util.Date;

public class RuleSelectAllResponse {
    /**
     *
     */
    public RuleSelectAllResponse(WmsRule rule) {
        this.id = rule.getId();
        this.name = rule.getName();
        this.task_type = rule.getTask_type();
        this.warehouse_id = rule.getWarehouse_id();
        this.location_expression = rule.getLocation_expression();
        this.resource_id_list = rule.getResource_id_list();
        this.people_name = rule.getPeople_name();
        this.note = rule.getNote();
        this.enabled = rule.getEnabled();
        this.del_flag = rule.getDel_flag();
        this.create_time = rule.getCreate_time();
        this.update_time = rule.getUpdate_time();
    }

    private Long id;
    private String name;
    private String task_type;
    private Long warehouse_id;
    private String location_expression;
    private String resource_id_list;
    private String people_name;
    private String note;
    private Integer enabled;
    private Integer del_flag;
    private Date create_time;
    private Date update_time;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTask_type() {
        return task_type;
    }

    public void setTask_type(String task_type) {
        this.task_type = task_type;
    }

    public Long getWarehouse_id() {
        return warehouse_id;
    }

    public void setWarehouse_id(Long id) {
        this.warehouse_id = warehouse_id;
    }

    public String getLocation_expression() {
        return location_expression;
    }

    public void setLocation_expression(String location_expression) {
        this.location_expression = location_expression;
    }

    public String getResource_id_list() {
        return resource_id_list;
    }

    public void setResource_id_list(String resource_id_list) {
        this.resource_id_list = resource_id_list;
    }

    public String getPeople_name() {
        return people_name;
    }

    public void setPeople_name(String people_name) {
        this.people_name = people_name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public Integer getDel_flag() {
        return del_flag;
    }

    public void setDel_flag(Integer del_flag) {
        this.del_flag = del_flag;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

}
