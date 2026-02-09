package com.yulgnier.web.app.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yulgnier.model.entity.ApartmentInfo;
import com.yulgnier.model.entity.FacilityInfo;
import com.yulgnier.web.app.vo.apartment.ApartmentItemVo;

import java.util.List;

/**
* @author liubo
* @description 针对表【apartment_info(公寓信息表)】的数据库操作Mapper
* @createDate 2023-07-26 11:12:39
* @Entity com.atguigu.lease.model.entity.ApartmentInfo
*/
public interface ApartmentInfoMapper extends BaseMapper<ApartmentInfo> {

    List<FacilityInfo> selectFacilityInfoListByApartmentId(Long id);

    ApartmentItemVo selectApartmentItemVoById(Long id);
}




