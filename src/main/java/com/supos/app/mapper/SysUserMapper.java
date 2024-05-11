package com.supos.app.mapper;

import com.supos.app.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SysUserMapper {

    SysUser queryById(Long id);

    List<SysUser> queryAll();

    long count(SysUser sysUser);

    int update(SysUser sysUser);

    int insertBatch(@Param("entities") List<SysUser> entities);

}

