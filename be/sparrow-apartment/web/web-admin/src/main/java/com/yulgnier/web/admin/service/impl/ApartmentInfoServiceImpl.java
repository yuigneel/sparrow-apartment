package com.yulgnier.web.admin.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yulgnier.model.enmu.ItemType;
import com.yulgnier.model.entity.*;
import com.yulgnier.web.admin.mapper.*;
import com.yulgnier.web.admin.service.*;
import com.yulgnier.web.admin.vo.apartment.ApartmentDetailVo;
import com.yulgnier.web.admin.vo.apartment.ApartmentItemVo;
import com.yulgnier.web.admin.vo.apartment.ApartmentQueryVo;
import com.yulgnier.web.admin.vo.apartment.ApartmentSubmitVo;
import com.yulgnier.web.admin.vo.fee.FeeValueVo;
import com.yulgnier.web.admin.vo.graph.GraphVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liubo
 * '@description 针对表【apartment_info(公寓信息表)】的数据库操作Service实现'
 * '@createDate 2023-07-24 15:48:00'
 */
@Service
public class ApartmentInfoServiceImpl extends ServiceImpl<ApartmentInfoMapper, ApartmentInfo>
        implements ApartmentInfoService {

    private final GraphInfoService graphInfoService;
    private final ApartmentFacilityService apartmentFacilityService;
    private final ApartmentLabelService apartmentLabelService;
    private final ApartmentFeeValueService apartmentFeeValueService;
    private final ApartmentInfoMapper apartmentInfoMapper;
    private final GraphInfoMapper graphInfoMapper;
    private final LabelInfoMapper labelInfoMapper;
    private final FacilityInfoMapper facilityInfoMapper;
    private final FeeValueMapper feeValueMapper;


    @Autowired

    public ApartmentInfoServiceImpl(GraphInfoService graphInfoService, ApartmentFacilityService apartmentFacilityService, ApartmentLabelService apartmentLabelService, ApartmentFeeValueService apartmentFeeValueService, ApartmentInfoMapper apartmentInfoMapper, GraphInfoMapper graphInfoMapper, LabelInfoMapper labelInfoMapper, FacilityInfoMapper facilityInfoMapper, FeeValueMapper feeValueMapper) {
        this.graphInfoService = graphInfoService;
        this.apartmentFacilityService = apartmentFacilityService;
        this.apartmentLabelService = apartmentLabelService;
        this.apartmentFeeValueService = apartmentFeeValueService;
        this.apartmentInfoMapper = apartmentInfoMapper;
        this.graphInfoMapper = graphInfoMapper;
        this.labelInfoMapper = labelInfoMapper;
        this.facilityInfoMapper = facilityInfoMapper;
        this.feeValueMapper = feeValueMapper;
    }

    @Override
    public void saveOrUpdateApartment(ApartmentSubmitVo apartmentSubmitVo) {
        // ========== 关键改动1：VO转Entity ==========
        ApartmentInfo apartmentInfo = new ApartmentInfo();
        // 把VO的属性拷贝到Entity（要求VO和Entity的属性名、类型一致）
        BeanUtils.copyProperties(apartmentSubmitVo, apartmentInfo);
        boolean isUpdate = apartmentSubmitVo.getId() != null;
        // ========== 关键改动2：传入Entity对象 ==========
        super.saveOrUpdate(apartmentInfo);  //super. *调用的父类方法
        // ========== 关键改动3：新增场景下，把生成的ID回写到VO ==========
        // 因为新增时ID是数据库自增/雪花算法生成的，VO里的ID原本是空的，必须回写
        if (!isUpdate) {
            apartmentSubmitVo.setId(apartmentInfo.getId());
        }
        if (isUpdate) {
            //删除图片列表
            LambdaQueryWrapper<GraphInfo> apartmentInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            apartmentInfoLambdaQueryWrapper.eq(GraphInfo::getItemType, ItemType.APARTMENT);
            apartmentInfoLambdaQueryWrapper.eq(GraphInfo::getItemId, apartmentSubmitVo.getId());
            graphInfoService.remove(apartmentInfoLambdaQueryWrapper);
            //删除配套列表
            LambdaQueryWrapper<ApartmentFacility> apartmentFacilityLambdaQueryWrapper = new LambdaQueryWrapper<>();
            apartmentFacilityLambdaQueryWrapper.eq(ApartmentFacility::getApartmentId, apartmentSubmitVo.getId());
            apartmentFacilityService.remove(apartmentFacilityLambdaQueryWrapper);
            //删除标签列表
            LambdaQueryWrapper<ApartmentLabel> apartmentLabelLambdaQueryWrapper = new LambdaQueryWrapper<>();
            apartmentLabelLambdaQueryWrapper.eq(ApartmentLabel::getApartmentId, apartmentSubmitVo.getId());
            apartmentLabelService.remove(apartmentLabelLambdaQueryWrapper);
            //删除费用列表
            LambdaQueryWrapper<ApartmentFeeValue> apartmentFeeValueLambdaQueryWrapper = new LambdaQueryWrapper<>();
            apartmentFeeValueLambdaQueryWrapper.eq(ApartmentFeeValue::getApartmentId, apartmentSubmitVo.getId());
            apartmentFeeValueService.remove(apartmentFeeValueLambdaQueryWrapper);
        }
        //插入图片列表
        List<GraphVo> graphVoList = apartmentSubmitVo.getGraphVoList();
        if (!CollectionUtils.isEmpty(graphVoList)) {
            ArrayList<GraphInfo> graphInfos = new ArrayList<>();
            for (GraphVo graphVo : graphVoList) {
                GraphInfo graphInfo = new GraphInfo();
                graphInfo.setItemType(ItemType.APARTMENT);
                graphInfo.setName(graphVo.getName());
                graphInfo.setItemId(apartmentSubmitVo.getId());
                graphInfo.setUrl(graphVo.getUrl());
                graphInfos.add(graphInfo);
            }
            graphInfoService.saveBatch(graphInfos);
        }
        //插入配套列表
        List<Long> facilityInfoIds = apartmentSubmitVo.getFacilityInfoIds();
        if (!CollectionUtils.isEmpty(facilityInfoIds)) {
            ArrayList<ApartmentFacility> apartmentFacilities = new ArrayList<>();
            for (Long facilityInfoId : facilityInfoIds) {
                ApartmentFacility apartmentFacility = new ApartmentFacility();
                apartmentFacility.setApartmentId(apartmentSubmitVo.getId());
                apartmentFacility.setFacilityId(facilityInfoId);
                apartmentFacilities.add(apartmentFacility);
            }
            apartmentFacilityService.saveBatch(apartmentFacilities);
        }
        //插入标签列表
        List<Long> labelIds = apartmentSubmitVo.getLabelIds();
        if (!CollectionUtils.isEmpty(labelIds)) {
            ArrayList<ApartmentLabel> apartmentLabels = new ArrayList<>();
            for (Long labelId : labelIds) {
                ApartmentLabel apartmentLabel = new ApartmentLabel();
                apartmentLabel.setApartmentId(apartmentSubmitVo.getId());
                apartmentLabel.setLabelId(labelId);
                apartmentLabels.add(apartmentLabel);
            }
            apartmentLabelService.saveBatch(apartmentLabels);
        }
        //插入费用列表
        List<Long> feeValueIds = apartmentSubmitVo.getFeeValueIds();
        if (!CollectionUtils.isEmpty(feeValueIds)) {
            ArrayList<ApartmentFeeValue> apartmentFeeValues = new ArrayList<>();
            for (Long feeValueId : feeValueIds) {
                ApartmentFeeValue apartmentFeeValue = new ApartmentFeeValue();
                apartmentFeeValue.setApartmentId(apartmentSubmitVo.getId());
                apartmentFeeValue.setFeeValueId(feeValueId);
                apartmentFeeValues.add(apartmentFeeValue);
            }
            apartmentFeeValueService.saveBatch(apartmentFeeValues);
        }
    }

    @Override
    public IPage<ApartmentItemVo> pageItem(Page<ApartmentItemVo> apartmentItemVoPage, ApartmentQueryVo queryVo) {
        return apartmentInfoMapper.pageItem(apartmentItemVoPage, queryVo);
    }

    @Override
    public ApartmentDetailVo getDetailById(Long id) {
        //1.查询公寓信息
        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectById(id);
        //2.查询图片列表
        List<GraphVo> graphVoList = graphInfoMapper.selectGraphVoList(id, ItemType.APARTMENT);
        //3.查询标签列表
        List<LabelInfo> labelInfoList = labelInfoMapper.selectListByApartment(id);
        //4.查询配套列表
        List<FacilityInfo> facilityInfoList = facilityInfoMapper.selectListByApartment(id);
        //5.查询费用列表
        List<FeeValueVo> feeValueVoList = feeValueMapper.selectFeeValueVoList(id);
        //6.封装VO
        ApartmentDetailVo apartmentDetailVo = new ApartmentDetailVo();
        BeanUtils.copyProperties(apartmentInfo, apartmentDetailVo);
        apartmentDetailVo.setGraphVoList(graphVoList);
        apartmentDetailVo.setLabelInfoList(labelInfoList);
        apartmentDetailVo.setFacilityInfoList(facilityInfoList);
        apartmentDetailVo.setFeeValueVoList(feeValueVoList);
        return apartmentDetailVo;
    }
}




