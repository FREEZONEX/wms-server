package com.supos.app.domain.entity;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

@Data
public class SuposUser implements Serializable {

    private static final long serialVersionUID = -43850712788255730L;

    private Long id;

    private String username;

    private Integer accounttype;

    private Integer lockstatus;

    private Integer valid;

    private String personcode;

    private String personname;

    private Integer delflag;

    private Date createtime;

    private Date modifytime;

    private Date synctime;

    private String password;
}

