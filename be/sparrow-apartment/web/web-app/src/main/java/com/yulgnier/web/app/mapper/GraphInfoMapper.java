package com.yulgnier.web.app.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yulgnier.model.entity.GraphInfo;
import com.yulgnier.web.app.vo.graph.GraphVo;

import java.util.List;

/**
* @author liubo
* @description 针对表【graph_info(图片信息表)】的数据库操作Mapper
* @createDate 2023-07-26 11:12:39
* @Entity com.atguigu.lease.model.entity.GraphInfo
*/
public interface GraphInfoMapper extends BaseMapper<GraphInfo> {

    List<GraphVo> selectGraphVoListByApartmentId(Long apartmentId);
}




