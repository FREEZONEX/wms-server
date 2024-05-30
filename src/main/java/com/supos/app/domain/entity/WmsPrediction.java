package com.supos.app.domain.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class WmsPrediction implements Serializable {

    private String date;
    private String count;
}