package com.supos.app.service;

import com.supos.app.domain.entity.WmsMaterial;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface WmsMaterialService extends IService<WmsMaterial> {
    List<WmsMaterial> selectAllForOutbound(WmsMaterial wmsMaterial);
}
