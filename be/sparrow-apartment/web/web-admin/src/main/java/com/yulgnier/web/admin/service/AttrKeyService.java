package com.yulgnier.web.admin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yulgnier.model.entity.AttrKey;
import com.yulgnier.web.admin.vo.attr.AttrKeyVo;

import java.util.List;

/**
* @author liubo
* @description 针对表【attr_key(房间基本属性表)】的数据库操作Service
* @createDate 2023-07-24 15:48:00
*/
public interface AttrKeyService extends IService<AttrKey> {

    /**
     * 查询全部属性名称和属性值列表
     * @return 属性名称和属性值列表
     */
    List<AttrKeyVo> listAttrInfo();
}
