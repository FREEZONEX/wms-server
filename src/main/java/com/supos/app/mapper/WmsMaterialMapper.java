package com.supos.app.mapper;
import org.apache.ibatis.annotations.Mapper;

import com.supos.app.domain.entity.WmsMaterial;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

@Mapper
public interface WmsMaterialMapper extends BaseMapper<WmsMaterial> {

    int insertSelective(WmsMaterial wmsMaterial);

    int updateSelective(WmsMaterial wmsMaterial);

    int deleteWmsMaterialById(WmsMaterial wmsMaterial);

    List<WmsMaterial> selectAll(WmsMaterial wmsMaterial);
}




