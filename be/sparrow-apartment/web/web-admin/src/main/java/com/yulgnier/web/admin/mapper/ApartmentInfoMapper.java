package com.yulgnier.web.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yulgnier.model.entity.ApartmentInfo;
import com.yulgnier.web.admin.vo.apartment.ApartmentItemVo;
import com.yulgnier.web.admin.vo.apartment.ApartmentQueryVo;
import org.apache.ibatis.annotations.Param; // 新增：导入@Param注解

/**
 * @author liubo
 * @description 针对表【apartment_info(公寓信息表)】的数据库操作Mapper
 * @createDate 2023-07-24 15:48:00
 * @Entity com.atguigu.lease.model.ApartmentInfo
 */
public interface ApartmentInfoMapper extends BaseMapper<ApartmentInfo> {

    // 改动1：加@Param("queryVo") 注解，让MyBatis识别XML中的queryVo参数
    // 改动2：返回值从void改为IPage<ApartmentItemVo>（规范分页写法，避免解析歧义）
    IPage<ApartmentItemVo> pageItem(Page<ApartmentItemVo> apartmentItemVoPage, @Param("queryVo") ApartmentQueryVo queryVo);
}