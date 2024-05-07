package com.supos.app.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName wms_rule
 */
@Data
public class WmsRule implements Serializable {

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

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        WmsRule other = (WmsRule) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
                && (this.getTask_type() == null ? other.getTask_type() == null : this.getTask_type().equals(other.getTask_type()))
                && (this.getWarehouse_id() == null ? other.getWarehouse_id() == null : this.getWarehouse_id().equals(other.getWarehouse_id()))
                && (this.getLocation_expression() == null ? other.getLocation_expression() == null : this.getLocation_expression().equals(other.getLocation_expression()))
                && (this.getResource_id_list() == null ? other.getResource_id_list() == null : this.getResource_id_list().equals(other.getResource_id_list()))
                && (this.getPeople_name() == null ? other.getPeople_name() == null : this.getPeople_name().equals(other.getPeople_name()))
                && (this.getNote() == null ? other.getNote() == null : this.getNote().equals(other.getNote()))
                && (this.getEnabled() == null ? other.getEnabled() == null : this.getEnabled().equals(other.getEnabled()))
                && (this.getDel_flag() == null ? other.getDel_flag() == null : this.getDel_flag().equals(other.getDel_flag()))
                && (this.getCreate_time() == null ? other.getCreate_time() == null : this.getCreate_time().equals(other.getCreate_time()))
                && (this.getUpdate_time() == null ? other.getUpdate_time() == null : this.getUpdate_time().equals(other.getUpdate_time()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getTask_type() == null) ? 0 : getTask_type().hashCode());
        result = prime * result + ((getWarehouse_id() == null) ? 0 : getWarehouse_id().hashCode());
        result = prime * result + ((getLocation_expression() == null) ? 0 : getLocation_expression().hashCode());
        result = prime * result + ((getResource_id_list() == null) ? 0 : getResource_id_list().hashCode());
        result = prime * result + ((getPeople_name() == null) ? 0 : getPeople_name().hashCode());
        result = prime * result + ((getNote() == null) ? 0 : getNote().hashCode());
        result = prime * result + ((getEnabled() == null) ? 0 : getEnabled().hashCode());
        result = prime * result + ((getDel_flag() == null) ? 0 : getDel_flag().hashCode());
        result = prime * result + ((getCreate_time() == null) ? 0 : getCreate_time().hashCode());
        result = prime * result + ((getUpdate_time() == null) ? 0 : getUpdate_time().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", task_type=").append(task_type);
        sb.append(", warehouse_id=").append(warehouse_id);
        sb.append(", location_expression=").append(location_expression);
        sb.append(", resource_id_list=").append(resource_id_list);
        sb.append(", people_name=").append(people_name);
        sb.append(", note=").append(note);
        sb.append(", enabled=").append(enabled);
        sb.append(", del_flag=").append(del_flag);
        sb.append(", create_time=").append(create_time);
        sb.append(", update_time=").append(update_time);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

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

    public Integer getEnabled() { return enabled; }

    public void setEnabled(Integer enabled) { this.enabled = enabled; }

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