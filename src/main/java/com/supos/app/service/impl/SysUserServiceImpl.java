package com.supos.app.service.impl;

import com.supos.app.entity.SysUser;
import com.supos.app.mapper.SysUserMapper;
import com.supos.app.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserDao;

    @Override
    public SysUser queryById(Long id) {
        return this.sysUserDao.queryById(id);
    }

    @Override
    public List<SysUser> queryAll() {
        return this.sysUserDao.queryAll();
    }

    @Override
    public int update(SysUser sysUsers) {
        return this.sysUserDao.update(sysUsers);
    }

    @Override
    public int insertBatch(List<SysUser> sysUsers) {
        return this.sysUserDao.insertBatch(sysUsers);
    }

}
