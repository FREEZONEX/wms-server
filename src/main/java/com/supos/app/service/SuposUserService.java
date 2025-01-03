package com.supos.app.service;

import com.supos.app.domain.entity.SuposUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SuposUserService {
    SuposUser selectById(Long id);

    List<SuposUser> selectAll();

    int update(SuposUser sysUser);

    int insertBatch(@Param("entities") List<SuposUser> entities);

    SuposUser login(String username,String password);
}
