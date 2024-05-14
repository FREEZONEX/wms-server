package com.supos.app.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class WmsTaskResource implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long task_id;
    private Long resource_id;
    private Date create_time;
    private Date update_time;
}