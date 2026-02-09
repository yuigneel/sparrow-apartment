package com.yulgnier.web.app.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yulgnier.model.entity.LabelInfo;

import java.util.List;

/**
* @author liubo
* @description 针对表【label_info(标签信息表)】的数据库操作Mapper
* @createDate 2023-07-26 11:12:39
* @Entity com.atguigu.lease.model.entity.LabelInfo
*/
public interface LabelInfoMapper extends BaseMapper<LabelInfo> {

    List<LabelInfo> selectLabelInfoByRoomId(Long id);

    List<LabelInfo> selectLabelInfoByApartmentId(Long id);
}




