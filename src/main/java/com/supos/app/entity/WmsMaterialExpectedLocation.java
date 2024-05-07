package com.supos.app.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 * @TableName wms_material_expected_location
 */
@Data
public class WmsMaterialExpectedLocation implements Serializable {
    /**
     *
     */
    private Long id;

    /**
     *
     */
    private Long material_id;

    /**
     *
     */
    private Long location_id;

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
        WmsMaterialExpectedLocation other = (WmsMaterialExpectedLocation) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getMaterial_id() == null ? other.getMaterial_id() == null : this.getMaterial_id().equals(other.getMaterial_id()))
                && (this.getLocation_id() == null ? other.getLocation_id() == null : this.getLocation_id().equals(other.getLocation_id()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getMaterial_id() == null) ? 0 : getMaterial_id().hashCode());
        result = prime * result + ((getLocation_id() == null) ? 0 : getLocation_id().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", material_id=").append(material_id);
        sb.append(", location_id=").append(location_id);
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

    public Long getMaterial_id() {
        return material_id;
    }

    public void setMaterial_id(Long material_id) {
        this.material_id = material_id;
    }

    public Long getLocation_id() {
        return location_id;
    }

    public void setLocation_id(Long location_id) {
        this.location_id = location_id;
    }
}