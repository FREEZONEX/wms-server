package com.supos.app.vo;

import com.supos.app.entity.WmsTask;

import java.util.Date;

public class TaskSelectAllResponse {

    public TaskSelectAllResponse(WmsTask task) {
        this.id = task.getId();
        this.operation_id = task.getOperation_id();
        this.type = task.getType();
        this.note = task.getNote();
        this.status = task.getStatus();
        this.people_name = task.getPeople_name();
        this.del_flag = task.getDel_flag();
        this.create_time = task.getCreate_time();
        this.update_time = task.getUpdate_time();
    }

    private Long id;
    private Long operation_id;
    private String type;
    private String note;
    private String status;
    private String people_name;
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

    public Long getOperation_id() {
        return operation_id;
    }

    public void setOperation_id(Long operation_id) {
        this.operation_id = operation_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPeople_name() {
        return people_name;
    }

    public void setPeople_name(String people_name) {
        this.people_name = people_name;
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
