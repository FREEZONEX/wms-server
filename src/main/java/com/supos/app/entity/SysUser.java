package com.supos.app.entity;

import java.util.Date;
import java.io.Serializable;

public class SysUser implements Serializable {

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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAccounttype() {
        return accounttype;
    }

    public void setAccounttype(Integer accounttype) {
        this.accounttype = accounttype;
    }

    public Integer getLockstatus() {
        return lockstatus;
    }

    public void setLockstatus(Integer lockstatus) {
        this.lockstatus = lockstatus;
    }

    public Integer getValid() {
        return valid;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }

    public String getPersoncode() {
        return personcode;
    }

    public void setPersoncode(String personcode) {
        this.personcode = personcode;
    }

    public String getPersonname() {
        return personname;
    }

    public void setPersonname(String personname) {
        this.personname = personname;
    }

    public Integer getDelflag() {
        return delflag;
    }

    public void setDelflag(Integer delflag) {
        this.delflag = delflag;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
    }

    public Date getSynctime() {
        return synctime;
    }

    public void setSynctime(Date synctime) {
        this.synctime = synctime;
    }

}

