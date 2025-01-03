package com.supos.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supos.app.domain.entity.WmsRfidMaterial;
import com.supos.app.service.WmsRfidMaterialService;
import com.supos.app.mapper.WmsRfidMaterialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class WmsRfidMaterialServiceImpl extends ServiceImpl<WmsRfidMaterialMapper, WmsRfidMaterial>
    implements WmsRfidMaterialService{

    @Autowired
    private WmsRfidMaterialMapper wmsRfidMaterialMapper;

    public int insertSelective(WmsRfidMaterial wmsRfidMaterial){
        return wmsRfidMaterialMapper.insertSelective(wmsRfidMaterial);
    }

    public int updateSelective(WmsRfidMaterial wmsRfidMaterial){
        return wmsRfidMaterialMapper.updateSelective(wmsRfidMaterial);
    }

    public int deleteSelective(WmsRfidMaterial wmsRfidMaterial){
        return wmsRfidMaterialMapper.deleteSelective(wmsRfidMaterial);
    }

    public List<WmsRfidMaterial> selectall(WmsRfidMaterial wmsRfidMaterial){
        return wmsRfidMaterialMapper.selectall(wmsRfidMaterial);
    }

}




