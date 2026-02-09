package com.yulgnier.web.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yulgnier.common.login.LoginUserHolder;
import com.yulgnier.model.enmu.ItemType;
import com.yulgnier.model.entity.*;
import com.yulgnier.web.app.mapper.*;
import com.yulgnier.web.app.service.BrowsingHistoryService;
import com.yulgnier.web.app.service.RoomInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yulgnier.web.app.vo.apartment.ApartmentItemVo;
import com.yulgnier.web.app.vo.attr.AttrValueVo;
import com.yulgnier.web.app.vo.fee.FeeValueVo;
import com.yulgnier.web.app.vo.room.RoomDetailVo;
import com.yulgnier.web.app.vo.room.RoomItemVo;
import com.yulgnier.web.app.vo.room.RoomQueryVo;
import com.yulgnier.web.app.vo.graph.GraphVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liubo
 * @description 针对表【room_info(房间信息表)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
@Slf4j
public class RoomInfoServiceImpl extends ServiceImpl<RoomInfoMapper, RoomInfo>
        implements RoomInfoService {
    private final RoomInfoMapper roomInfoMapper;
    private final ApartmentInfoMapper apartmentInfoMapper;
    private final GraphInfoMapper graphInfoMapper;
    private final FacilityInfoMapper facilityInfoMapper;
    private final LabelInfoMapper labelInfoMapper;
    private final PaymentTypeMapper paymentTypeMapper;
    private final FeeValueMapper feeValueMapper;
    private final LeaseTermMapper leaseTermMapper;
    private final BrowsingHistoryService browsingHistoryService;

    public RoomInfoServiceImpl(RoomInfoMapper roomInfoMapper, ApartmentInfoMapper apartmentInfoMapper, GraphInfoMapper graphInfoMapper, FacilityInfoMapper facilityInfoMapper, LabelInfoMapper labelInfoMapper, PaymentTypeMapper paymentTypeMapper, FeeValueMapper feeValueMapper, LeaseTermMapper leaseTermMapper, BrowsingHistoryService browsingHistoryService) {
        this.roomInfoMapper = roomInfoMapper;
        this.apartmentInfoMapper = apartmentInfoMapper;
        this.graphInfoMapper = graphInfoMapper;
        this.facilityInfoMapper = facilityInfoMapper;
        this.labelInfoMapper = labelInfoMapper;
        this.paymentTypeMapper = paymentTypeMapper;
        this.feeValueMapper = feeValueMapper;
        this.leaseTermMapper = leaseTermMapper;
        this.browsingHistoryService = browsingHistoryService;
    }

    @Override
    public IPage<RoomItemVo> pageItem(Page<RoomItemVo> roomItemVoPage, RoomQueryVo queryVo) {
        return roomInfoMapper.pageItem(roomItemVoPage, queryVo);
    }

    @Override
    public RoomDetailVo getRoomDetailVoById(Long id) {
        //获取房间信息
        RoomInfo roomInfo = roomInfoMapper.selectById(id);
        //获取所属公寓信息
        LambdaQueryWrapper<ApartmentInfo> apartmentInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        apartmentInfoLambdaQueryWrapper.eq(ApartmentInfo::getId, roomInfo.getApartmentId());
        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectOne(apartmentInfoLambdaQueryWrapper);
        //获取公寓标签
        List<LabelInfo> labelInfoListApartment= labelInfoMapper.selectLabelInfoByApartmentId(apartmentInfo.getId());
        //获取公寓图片
        LambdaQueryWrapper<GraphInfo> graphInfoLambdaQueryWrapperApartment = new LambdaQueryWrapper<>();
        graphInfoLambdaQueryWrapperApartment.eq(GraphInfo::getItemType, ItemType.APARTMENT);
        graphInfoLambdaQueryWrapperApartment.eq(GraphInfo::getItemId, apartmentInfo.getId());
        List<GraphInfo> graphInfoListApartment = graphInfoMapper.selectList(graphInfoLambdaQueryWrapperApartment);
        ArrayList<GraphVo> graphVoApartments = new ArrayList<>();
        for (GraphInfo graphInfo : graphInfoListApartment) {
            graphVoApartments.add(new GraphVo(graphInfo.getName(), graphInfo.getUrl()));
        }
        //获取公寓最小租价
        BigDecimal minRent= roomInfoMapper.selectMinRentByApartmentId(apartmentInfo.getId());
        //组装公寓详情VO
        ApartmentItemVo apartmentItemVo = new ApartmentItemVo();
        BeanUtils.copyProperties(apartmentInfo, apartmentItemVo);
        apartmentItemVo.setLabelInfoList(labelInfoListApartment);
        apartmentItemVo.setGraphVoList(graphVoApartments);
        apartmentItemVo.setMinRent(minRent);
        //获取图片列表
        LambdaQueryWrapper<GraphInfo> graphInfoLambdaQueryWrapperRoom = new LambdaQueryWrapper<>();
        graphInfoLambdaQueryWrapperRoom.eq(GraphInfo::getItemType, ItemType.ROOM);
        graphInfoLambdaQueryWrapperRoom.eq(GraphInfo::getItemId, id);
        List<GraphInfo> graphInfoListRoom = graphInfoMapper.selectList(graphInfoLambdaQueryWrapperRoom);
        ArrayList<GraphVo> graphVoRoomList = new ArrayList<>();
        for (GraphInfo graphInfo : graphInfoListRoom) {
            graphVoRoomList.add(new GraphVo(graphInfo.getName(), graphInfo.getUrl()));
        }
        //获取属性信息列表
        List<AttrValueVo> attrValueVoList = roomInfoMapper.getAttrValueVoListByRoomId(id);
        //获取配套信息列表
        List<FacilityInfo> facilityInfoList = facilityInfoMapper.selectFacilityInfoByRoomId(id);
        //获取标签信息列表
        List<LabelInfo> labelInfoListRoom = labelInfoMapper.selectLabelInfoByRoomId(id);
        //获取支付方式列表
        List<PaymentType> paymentTypeList = paymentTypeMapper.selectPaymentTypeByRoomId(id);
        //获取杂费列表
        List<FeeValueVo> feeValueVoList = feeValueMapper.selectFeeValueVoListByApartmentId(apartmentInfo.getId());
        //获取租期列表
        List<LeaseTerm> leaseTermList = leaseTermMapper.selectLeaseTermByRoomId(id);
        //组装房间详情VO
        RoomDetailVo roomDetailVo = new RoomDetailVo();
        BeanUtils.copyProperties(roomInfo, roomDetailVo);
        roomDetailVo.setApartmentItemVo(apartmentItemVo);
        roomDetailVo.setGraphVoList(graphVoRoomList);
        roomDetailVo.setAttrValueVoList(attrValueVoList);
        roomDetailVo.setFacilityInfoList(facilityInfoList);
        roomDetailVo.setLabelInfoList(labelInfoListRoom);
        roomDetailVo.setPaymentTypeList(paymentTypeList);
        roomDetailVo.setFeeValueVoList(feeValueVoList);
        roomDetailVo.setLeaseTermList(leaseTermList);
        //保存浏览历史
        browsingHistoryService.saveHistory(LoginUserHolder.getLoginUser().getUserId(),id);
        return roomDetailVo;
    }

    @Override
    public IPage<RoomItemVo> pageItemByApartmentId(Page<RoomItemVo> roomItemVoPage, Long id) {
       return roomInfoMapper.pageItemByApartmentId(roomItemVoPage, id);
    }
}




