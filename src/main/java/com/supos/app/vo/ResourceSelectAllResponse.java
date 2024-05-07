package com.supos.app.vo;

import com.supos.app.entity.WmsResource;

import java.util.Date;
import java.util.List;

public class ResourceSelectAllResponse {
    /**
     *
     */
    public ResourceSelectAllResponse(WmsResource resource) {
        this.id = resource.getId();
        this.name = resource.getName();
        this.type = resource.getType();
        this.note = resource.getNote();
        this.del_flag = resource.getDel_flag();
        this.create_time = resource.getCreate_time();
        this.update_time = resource.getUpdate_time();
    }

    private Long id;
    private String name;
    private String type;
    private String note;
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
