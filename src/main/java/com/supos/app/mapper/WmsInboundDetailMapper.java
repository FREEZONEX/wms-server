package com.supos.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.supos.app.entity.WmsInventoryOperationDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface WmsInboundDetailMapper extends BaseMapper<WmsInventoryOperationDetail> {
    int insertSelective(WmsInventoryOperationDetail wmsInventoryOperationDetail);

    int updateRecordById(WmsInventoryOperationDetail wmsInventoryOperationDetail);

    int deleteRecordById(WmsInventoryOperationDetail wmsInventoryOperationDetail);

    List<WmsInventoryOperationDetail> selectAll(WmsInventoryOperationDetail wmsInventoryOperationDetail);
}




