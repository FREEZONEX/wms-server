package com.supos.app.domain.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class WmsResourceOccupyLog implements Serializable {

    private Long id;

    private Long task_id;

    private Long resource_id;

    private Integer is_occupy;

    private Date create_time;

    private Date update_time;

    private String resource_name;

    private String type;

    private Long total_time_diff_seconds;

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
        WmsResourceOccupyLog other = (WmsResourceOccupyLog) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getTask_id() == null ? other.getTask_id() == null : this.getTask_id().equals(other.getTask_id()))
            && (this.getResource_id() == null ? other.getResource_id() == null : this.getResource_id().equals(other.getResource_id()))
            && (this.getIs_occupy() == null ? other.getIs_occupy() == null : this.getIs_occupy().equals(other.getIs_occupy()))
            && (this.getCreate_time() == null ? other.getCreate_time() == null : this.getCreate_time().equals(other.getCreate_time()))
            && (this.getUpdate_time() == null ? other.getUpdate_time() == null : this.getUpdate_time().equals(other.getUpdate_time()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTask_id() == null) ? 0 : getTask_id().hashCode());
        result = prime * result + ((getResource_id() == null) ? 0 : getResource_id().hashCode());
        result = prime * result + ((getIs_occupy() == null) ? 0 : getIs_occupy().hashCode());
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
        sb.append(", task_id=").append(task_id);
        sb.append(", resource_id=").append(resource_id);
        sb.append(", is_occupy=").append(is_occupy);
        sb.append(", create_time=").append(create_time);
        sb.append(", update_time=").append(update_time);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}