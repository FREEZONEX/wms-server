package com.supos.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.supos.app.domain.entity.WmsTask;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WmsTaskMapper extends BaseMapper<WmsTask> {
    int insertSelective(WmsTask wmsTask);

    int updateTaskById(WmsTask wmsTask);

    int deleteTaskById(WmsTask wmsTask);

    List<WmsTask> selectAll(WmsTask wmsTask);

    int selectCount(WmsTask wmsTask);
}




