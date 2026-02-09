package com.yulgnier.web.app.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yulgnier.model.entity.PaymentType;

import java.util.List;

/**
* @author liubo
* @description 针对表【payment_type(支付方式表)】的数据库操作Mapper
* @createDate 2023-07-26 11:12:39
* @Entity com.atguigu.lease.model.entity.PaymentType
*/
public interface PaymentTypeMapper extends BaseMapper<PaymentType> {

    List<PaymentType> listByRoomId(Long id);

    List<PaymentType> selectPaymentTypeByRoomId(Long id);
}




