package com.yulgnier.web.admin.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yulgnier.model.enmu.ReleaseStatus;
import com.yulgnier.model.entity.RoomInfo;
import com.yulgnier.web.admin.vo.room.RoomDetailVo;
import com.yulgnier.web.admin.vo.room.RoomItemVo;
import com.yulgnier.web.admin.vo.room.RoomQueryVo;
import com.yulgnier.web.admin.vo.room.RoomSubmitVo;

import java.util.List;

/**
* @author liubo
* @description 针对表【room_info(房间信息表)】的数据库操作Service
* @createDate 2023-07-24 15:48:00
*/
public interface RoomInfoService extends IService<RoomInfo> {

    void saveOrUpdateRoom(RoomSubmitVo roomSubmitVo);

    IPage<RoomItemVo> pageItem(IPage<RoomItemVo> roomItemVoPage, RoomQueryVo queryVo);

    RoomDetailVo getDetailById(Long id);

    void removeRoomById(Long id);

    void setStatusById(Long id, ReleaseStatus status);

    List<RoomInfo> listBasicByApartmentId(Long id);
}
