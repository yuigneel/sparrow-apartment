package com.yulgnier.web.app.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yulgnier.model.entity.FeeValue;
import com.yulgnier.web.app.vo.fee.FeeValueVo;

import java.util.List;

/**
* @author liubo
* @description 针对表【fee_value(杂项费用值表)】的数据库操作Mapper
* @createDate 2023-07-26 11:12:39
* @Entity com.atguigu.lease.model.entity.FeeValue
*/
public interface FeeValueMapper extends BaseMapper<FeeValue> {
    List<FeeValueVo> selectFeeValueVoListByApartmentId(Long id);
}




