package com.yulgnier.web.app.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yulgnier.model.entity.RoomInfo;
import com.yulgnier.web.app.vo.attr.AttrValueVo;
import com.yulgnier.web.app.vo.room.RoomItemVo;
import com.yulgnier.web.app.vo.room.RoomQueryVo;

import java.math.BigDecimal;
import java.util.List;

/**
* @author liubo
* @description 针对表【room_info(房间信息表)】的数据库操作Mapper
* @createDate 2023-07-26 11:12:39
* @Entity com.atguigu.lease.model.entity.RoomInfo
*/
public interface RoomInfoMapper extends BaseMapper<RoomInfo> {

    IPage<RoomItemVo> pageItem(Page<RoomItemVo> roomItemVoPage, RoomQueryVo queryVo);

    List<AttrValueVo> getAttrValueVoListByRoomId(Long id);

    BigDecimal selectMinRentByApartmentId(Long id);

    IPage<RoomItemVo> pageItemByApartmentId(Page<RoomItemVo> roomItemVoPage, Long id);
}