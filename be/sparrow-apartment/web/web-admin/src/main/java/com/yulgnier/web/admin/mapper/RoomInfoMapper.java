package com.yulgnier.web.admin.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yulgnier.model.entity.*;
import com.yulgnier.web.admin.vo.attr.AttrValueVo;
import com.yulgnier.web.admin.vo.room.RoomItemVo;
import com.yulgnier.web.admin.vo.room.RoomQueryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author liubo
* @description 针对表【room_info(房间信息表)】的数据库操作Mapper
* @createDate 2023-07-24 15:48:00
* @Entity com.atguigu.lease.model.RoomInfo
*/
public interface RoomInfoMapper extends BaseMapper<RoomInfo> {

    IPage<RoomItemVo> pageItem(IPage<RoomItemVo> roomItemVoPage, @Param("queryVo") RoomQueryVo queryVo);

    List<AttrValueVo> selectAttrValueListById(Long id);

    List<FacilityInfo> selectFacilityInfoListById(Long id);

    List<LabelInfo> selectLabelInfoListById(Long id);

    List<PaymentType> selectPaymentTypeListById(Long id);

    List<LeaseTerm> selectLeaseTermListById(Long id);
}




