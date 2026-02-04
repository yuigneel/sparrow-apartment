package com.yulgnier.web.admin.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yulgnier.model.enmu.ItemType;
import com.yulgnier.model.enmu.ReleaseStatus;
import com.yulgnier.model.entity.*;
import com.yulgnier.web.admin.mapper.*;
import com.yulgnier.web.admin.service.*;
import com.yulgnier.web.admin.vo.attr.AttrValueVo;
import com.yulgnier.web.admin.vo.graph.GraphVo;
import com.yulgnier.web.admin.vo.room.RoomDetailVo;
import com.yulgnier.web.admin.vo.room.RoomItemVo;
import com.yulgnier.web.admin.vo.room.RoomQueryVo;
import com.yulgnier.web.admin.vo.room.RoomSubmitVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liubo
 * @description 针对表【room_info(房间信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class RoomInfoServiceImpl extends ServiceImpl<RoomInfoMapper, RoomInfo>
        implements RoomInfoService {

    private final RoomLeaseTermService roomLeaseTermService;
    private final RoomLeaseTermMapper roomLeaseTermMapper;
    private final RoomPaymentTypeService roomPaymentTypeService;
    private final RoomPaymentTypeMapper roomPaymentTypeMapper;
    private final RoomAttrValueService roomAttrValueService;
    private final RoomAttrValueMapper roomAttrValueMapper;
    private final RoomLabelService roomLabelService;
    private final RoomLabelMapper roomLabelMapper;
    private final RoomFacilityService roomFacilityService;
    private final RoomFacilityMapper roomFacilityMapper;
    private final GraphInfoService graphInfoService;
    private final GraphInfoMapper graphInfoMapper;
    private final RoomInfoMapper roomInfoMapper;
    private final ApartmentInfoMapper apartmentInfoMapper;


    @Autowired
    public RoomInfoServiceImpl(RoomLeaseTermMapper roomLeaseTermMapper, RoomLeaseTermService roomLeaseTermService, RoomPaymentTypeService roomPaymentTypeService, RoomPaymentTypeMapper roomPaymentTypeMapper, RoomAttrValueService roomAttrValueService, RoomAttrValueMapper roomAttrValueMapper, RoomLabelService roomLabelService, RoomLabelMapper roomLabelMapper, RoomFacilityService roomFacilityService, RoomFacilityMapper roomFacilityMapper, GraphInfoService graphInfoService, GraphInfoMapper graphInfoMapper, RoomInfoMapper roomInfoMapper, ApartmentInfoMapper apartmentInfoMapper) {
        this.roomLeaseTermMapper = roomLeaseTermMapper;
        this.roomLeaseTermService = roomLeaseTermService;
        this.roomPaymentTypeService = roomPaymentTypeService;
        this.roomPaymentTypeMapper = roomPaymentTypeMapper;
        this.roomAttrValueService = roomAttrValueService;
        this.roomAttrValueMapper = roomAttrValueMapper;
        this.roomLabelService = roomLabelService;
        this.roomLabelMapper = roomLabelMapper;
        this.roomFacilityService = roomFacilityService;
        this.roomFacilityMapper = roomFacilityMapper;
        this.graphInfoService = graphInfoService;
        this.graphInfoMapper = graphInfoMapper;
        this.roomInfoMapper = roomInfoMapper;
        this.apartmentInfoMapper = apartmentInfoMapper;
    }


    @Override
    public void saveOrUpdateRoom(RoomSubmitVo roomSubmitVo) {
        RoomInfo roomInfo = new RoomInfo();
        BeanUtils.copyProperties(roomSubmitVo, roomInfo);
        //1.判断id是否为null
        Long id = roomSubmitVo.getId();
        //2.如果不为空，则进行删除操作
        if (id != null) {
            //2.1删除租赁关系
            LambdaQueryWrapper<RoomLeaseTerm> roomLeaseTermLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roomLeaseTermLambdaQueryWrapper.in(RoomLeaseTerm::getLeaseTermId, roomSubmitVo.getLeaseTermIds());
            roomLeaseTermMapper.delete(roomLeaseTermLambdaQueryWrapper);
            //2.2删除支付关系
            LambdaQueryWrapper<RoomPaymentType> roomPaymentTypeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roomPaymentTypeLambdaQueryWrapper.in(RoomPaymentType::getPaymentTypeId, roomSubmitVo.getPaymentTypeIds());
            roomPaymentTypeMapper.delete(roomPaymentTypeLambdaQueryWrapper);
            //2.3删除属性值关系
            LambdaQueryWrapper<RoomAttrValue> roomAttrValueLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roomAttrValueLambdaQueryWrapper.in(RoomAttrValue::getAttrValueId, roomSubmitVo.getAttrValueIds());
            roomAttrValueMapper.delete(roomAttrValueLambdaQueryWrapper);
            //2.4删除标签关系
            LambdaQueryWrapper<RoomLabel> roomLabelLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roomLabelLambdaQueryWrapper.in(RoomLabel::getLabelId, roomSubmitVo.getLabelInfoIds());
            roomLabelMapper.delete(roomLabelLambdaQueryWrapper);
            //2.5删除配套关系
            LambdaQueryWrapper<RoomFacility> roomFacilityLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roomFacilityLambdaQueryWrapper.in(RoomFacility::getFacilityId, roomSubmitVo.getFacilityInfoIds());
            roomFacilityMapper.delete(roomFacilityLambdaQueryWrapper);
            //2.6删除图片信息
            LambdaQueryWrapper<GraphInfo> graphInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            graphInfoLambdaQueryWrapper.eq(GraphInfo::getItemType, ItemType.ROOM);
            graphInfoLambdaQueryWrapper.eq(GraphInfo::getItemId, roomSubmitVo.getId());
            graphInfoMapper.delete(graphInfoLambdaQueryWrapper);
        }
        //3.进行保存操作
        //3.1插入房间信息
        roomInfoMapper.insert(roomInfo);
        //3.2插入租赁关系
        List<Long> leaseTermIds = roomSubmitVo.getLeaseTermIds();
        if (!CollectionUtils.isEmpty(leaseTermIds)) {
            ArrayList<RoomLeaseTerm> roomLeaseTerms = new ArrayList<>();
            for (Long leaseTermId : leaseTermIds) {
                // 正确的builder模式使用：链式调用后用build()得到对象
                RoomLeaseTerm roomLeaseTerm = RoomLeaseTerm.builder()
                        .roomId(roomInfo.getId())
                        .leaseTermId(leaseTermId)
                        .build(); // 必须调用build()，否则无法生成有效对象
                roomLeaseTerms.add(roomLeaseTerm);
            }
            roomLeaseTermService.saveBatch(roomLeaseTerms);
        }
        //3.3插入支付关系
        List<Long> paymentTypeIds = roomSubmitVo.getPaymentTypeIds();
        if (!CollectionUtils.isEmpty(paymentTypeIds)) {
            ArrayList<RoomPaymentType> roomPaymentTypes = new ArrayList<>();
            for (Long paymentTypeId : paymentTypeIds) {
                RoomPaymentType roomPaymentType = RoomPaymentType.builder()
                        .roomId(roomInfo.getId())
                        .paymentTypeId(paymentTypeId)
                        .build();
                roomPaymentTypes.add(roomPaymentType);
            }
            roomPaymentTypeService.saveBatch(roomPaymentTypes);
        }
        //3.4插入属性值关系
        List<Long> attrValueIds = roomSubmitVo.getAttrValueIds();
        if (!CollectionUtils.isEmpty(attrValueIds)) {
            ArrayList<RoomAttrValue> roomAttrValues = new ArrayList<>();
            for (Long attrValueId : attrValueIds) {
                RoomAttrValue roomAttrValue = RoomAttrValue.builder()
                        .roomId(roomInfo.getId())
                        .attrValueId(attrValueId)
                        .build();
                roomAttrValues.add(roomAttrValue);
            }
            roomAttrValueService.saveBatch(roomAttrValues);
        }
        //3.5插入标签关系
        List<Long> labelInfoIds = roomSubmitVo.getLabelInfoIds();
        if (!CollectionUtils.isEmpty(labelInfoIds)) {
            ArrayList<RoomLabel> roomLabels = new ArrayList<>();
            for (Long labelInfoId : labelInfoIds) {
                RoomLabel roomLabel = RoomLabel.builder()
                        .roomId(roomInfo.getId())
                        .labelId(labelInfoId)
                        .build();
                roomLabels.add(roomLabel);
            }
            roomLabelService.saveBatch(roomLabels);
        }
        //3.6插入配套关系
        List<Long> facilityInfoIds = roomSubmitVo.getFacilityInfoIds();
        if (!CollectionUtils.isEmpty(facilityInfoIds)) {
            ArrayList<RoomFacility> roomFacilities = new ArrayList<>();
            for (Long facilityInfoId : facilityInfoIds) {
                RoomFacility roomFacility = RoomFacility.builder()
                        .roomId(roomInfo.getId())
                        .facilityId(facilityInfoId)
                        .build();
                roomFacilities.add(roomFacility);
            }
            roomFacilityService.saveBatch(roomFacilities);
        }
        //3.7插入图片信息
        List<GraphVo> graphVoList = roomSubmitVo.getGraphVoList();
        if (!CollectionUtils.isEmpty(graphVoList)) {
            ArrayList<GraphInfo> graphInfos = new ArrayList<>();
            for (GraphVo graphVo : graphVoList) {

                GraphInfo graphInfo = new GraphInfo();
                graphInfo.setItemType(ItemType.ROOM);
                graphInfo.setItemId(roomInfo.getId());
                graphInfo.setName(graphVo.getName());
                graphInfo.setUrl(graphVo.getUrl());
                graphInfos.add(graphInfo);
            }
            graphInfoService.saveBatch(graphInfos);
        }
    }

    @Override
    public IPage<RoomItemVo> pageItem(IPage<RoomItemVo> roomItemVoPage, RoomQueryVo queryVo) {
        return roomInfoMapper.pageItem(roomItemVoPage, queryVo);
    }

    @Override
    public RoomDetailVo getDetailById(Long id) {
        //1.查询房间基本信息
        RoomInfo roomInfo = this.getById(id);
        //2.查询所属公寓信息
        LambdaQueryWrapper<ApartmentInfo> apartmentInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        apartmentInfoLambdaQueryWrapper.eq(ApartmentInfo::getId, roomInfo.getApartmentId());
        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectOne(apartmentInfoLambdaQueryWrapper);
        //3.查询图片列表信息
        LambdaQueryWrapper<GraphInfo> graphInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        graphInfoLambdaQueryWrapper.eq(GraphInfo::getItemType, ItemType.ROOM);
        graphInfoLambdaQueryWrapper.eq(GraphInfo::getItemId, id);
        List<GraphInfo> graphInfoList = graphInfoMapper.selectList(graphInfoLambdaQueryWrapper);
        ArrayList<GraphVo> graphVoList = new ArrayList<>();
        for (GraphInfo graphInfo : graphInfoList) {
            graphVoList.add(new GraphVo(graphInfo.getName(), graphInfo.getUrl()));
        }
        //4.查询属性列表信息
        List<AttrValueVo> attrValueVoList = roomInfoMapper.selectAttrValueListById(id);
        //5.查询标签列表信息
        List<LabelInfo> labelInfoList = roomInfoMapper.selectLabelInfoListById(id);
        //6.查询配套列表信息
        List<FacilityInfo> facilityInfoList = roomInfoMapper.selectFacilityInfoListById(id);
        //7.查询支付方式列表信息
        List<PaymentType> paymentTypeList = roomInfoMapper.selectPaymentTypeListById(id);
        //8.查询可选租期列表信息
        List<LeaseTerm> leaseTermList = roomInfoMapper.selectLeaseTermListById(id);
        //9.组装vo
        RoomDetailVo roomDetailVo = new RoomDetailVo();
        BeanUtils.copyProperties(roomInfo, roomDetailVo);
        roomDetailVo.setApartmentInfo(apartmentInfo);
        roomDetailVo.setGraphVoList(graphVoList);
        roomDetailVo.setAttrValueVoList(attrValueVoList);
        roomDetailVo.setLabelInfoList(labelInfoList);
        roomDetailVo.setFacilityInfoList(facilityInfoList);
        roomDetailVo.setPaymentTypeList(paymentTypeList);
        roomDetailVo.setLeaseTermList(leaseTermList);
        return roomDetailVo;
    }

    @Override
    @Transactional      // 开启事务
    public void removeRoomById(Long id) {
        //1.删除房间&基本属性关联表
        roomAttrValueService.remove(new LambdaQueryWrapper<RoomAttrValue>().eq(RoomAttrValue::getRoomId, id));
        //2.删除房间&配套关系表
        roomFacilityService.remove(new LambdaQueryWrapper<RoomFacility>().eq(RoomFacility::getRoomId, id));
        //3.删除房间&标签关系表
        roomLabelService.remove(new LambdaQueryWrapper<RoomLabel>().eq(RoomLabel::getRoomId, id));
        //4.删除房间&租期关系表
        roomLeaseTermService.remove(new LambdaQueryWrapper<RoomLeaseTerm>().eq(RoomLeaseTerm::getRoomId, id));
        //5.删除房间&支付方式关系表
        roomPaymentTypeService.remove(new LambdaQueryWrapper<RoomPaymentType>().eq(RoomPaymentType::getRoomId, id));
        //6.删除房间基本信息表
        this.removeById(id);
    }

    @Override
    public void setStatusById(Long id, ReleaseStatus status) {
        //1.获取房间对象
        RoomInfo roomInfo = this.getById(id);
        //2.修改状态
        roomInfo.setIsRelease(status);
        this.updateById(roomInfo);
    }

    @Override
    public List<RoomInfo> listBasicByApartmentId(Long id) {
        LambdaQueryWrapper<RoomInfo> roomInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roomInfoLambdaQueryWrapper.eq(RoomInfo::getApartmentId, id);
        return this.list(roomInfoLambdaQueryWrapper);
    }
}




