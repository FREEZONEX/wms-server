package com.supos.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.supos.app.domain.entity.WmsPrediction;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WmsPredictionMapper extends BaseMapper<WmsPrediction> {

    List<WmsPrediction> CalculatePrediction();
}
