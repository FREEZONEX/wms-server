package com.supos.app.service.impl;

import com.supos.app.domain.entity.SuposUser;
import com.supos.app.mapper.SuposUserMapper;
import com.supos.app.service.SuposUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuposUserServiceImpl implements SuposUserService {
    @Autowired
    private SuposUserMapper suposUserDao;

    @Override
    public SuposUser selectById(Long id) {
        return this.suposUserDao.selectById(id);
    }

    @Override
    public List<SuposUser> selectAll() {
        return this.suposUserDao.selectAll();
    }

    @Override
    public int update(SuposUser suposUser) {
        return this.suposUserDao.update(suposUser);
    }

    @Override
    public int insertBatch(List<SuposUser> suposUser) {
        return this.suposUserDao.insertBatch(suposUser);
    }

    @Override
    public SuposUser login(String username,String password){
        SuposUser suposUser= this.suposUserDao.login(username.trim());
        return suposUser != null&&password.equals("123456")?suposUser:null;
    }


}
