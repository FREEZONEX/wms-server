package com.supos.app.service;

import com.supos.app.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserService {
    SysUser queryById(Long id);

    List<SysUser> queryAll();

    int update(SysUser sysUser);

    int insertBatch(@Param("entities") List<SysUser> entities);

}
