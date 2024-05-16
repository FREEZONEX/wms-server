package com.supos.app.mapper;
import java.util.List;

import com.supos.app.domain.entity.WmsRfidMaterial;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface WmsRfidMaterialMapper extends BaseMapper<WmsRfidMaterial> {

    int insertSelective(WmsRfidMaterial wmsRfidMaterial);

    int updateSelective(WmsRfidMaterial wmsRfidMaterial);

    int deleteSelective(WmsRfidMaterial wmsRfidMaterial);

    List<WmsRfidMaterial> selectall(WmsRfidMaterial wmsRfidMaterial);


}




