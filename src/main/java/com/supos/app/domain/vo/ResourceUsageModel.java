package com.supos.app.domain.vo;

import lombok.Data;

@Data
public class ResourceUsageModel {
    private String name;
    private String occupyTotalTime;
    private float occupyRate;
    private float occupyMaxRate;
    private String idleTotalTime;
}
