package com.supos.app.mapper;

import com.supos.app.domain.entity.SuposUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SuposUserMapper {

    SuposUser selectById(Long id);

    List<SuposUser> selectAll();

    long count(SuposUser sysUser);

    int update(SuposUser sysUser);

    int insertBatch(@Param("entities") List<SuposUser> entities);

    SuposUser login(@Param("username") String username);
}

