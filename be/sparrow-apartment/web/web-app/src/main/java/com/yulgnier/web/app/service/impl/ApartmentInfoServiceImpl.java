package com.yulgnier.web.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yulgnier.model.enmu.ItemType;
import com.yulgnier.model.entity.ApartmentInfo;
import com.yulgnier.model.entity.FacilityInfo;
import com.yulgnier.model.entity.GraphInfo;
import com.yulgnier.model.entity.LabelInfo;
import com.yulgnier.web.app.mapper.ApartmentInfoMapper;
import com.yulgnier.web.app.mapper.GraphInfoMapper;
import com.yulgnier.web.app.mapper.LabelInfoMapper;
import com.yulgnier.web.app.mapper.RoomInfoMapper;
import com.yulgnier.web.app.service.ApartmentInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yulgnier.web.app.vo.apartment.ApartmentDetailVo;
import com.yulgnier.web.app.vo.apartment.ApartmentItemVo;
import com.yulgnier.web.app.vo.graph.GraphVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liubo
 * @description 针对表【apartment_info(公寓信息表)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
public class ApartmentInfoServiceImpl extends ServiceImpl<ApartmentInfoMapper, ApartmentInfo>
        implements ApartmentInfoService {
    private final ApartmentInfoMapper apartmentInfoMapper;
    private final LabelInfoMapper labelInfoMapper;
    private final GraphInfoMapper graphInfoMapper;
    private final RoomInfoMapper roomInfoMapper;

    public ApartmentInfoServiceImpl(ApartmentInfoMapper apartmentInfoMapper, LabelInfoMapper labelInfoMapper, GraphInfoMapper graphInfoMapper, RoomInfoMapper roomInfoMapper) {
        this.apartmentInfoMapper = apartmentInfoMapper;
        this.labelInfoMapper = labelInfoMapper;
        this.graphInfoMapper = graphInfoMapper;
        this.roomInfoMapper = roomInfoMapper;
    }

    @Override
    public ApartmentDetailVo getDetailById(Long id) {
        //获取所属公寓信息
        LambdaQueryWrapper<ApartmentInfo> apartmentInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        apartmentInfoLambdaQueryWrapper.eq(ApartmentInfo::getId, id);
        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectOne(apartmentInfoLambdaQueryWrapper);
        //获取公寓标签
        List<LabelInfo> labelInfoListApartment= labelInfoMapper.selectLabelInfoByApartmentId(id);
        //获取公寓图片
        LambdaQueryWrapper<GraphInfo> graphInfoLambdaQueryWrapperApartment = new LambdaQueryWrapper<>();
        graphInfoLambdaQueryWrapperApartment.eq(GraphInfo::getItemType, ItemType.APARTMENT);
        graphInfoLambdaQueryWrapperApartment.eq(GraphInfo::getItemId, id);
        List<GraphInfo> graphInfoListApartment = graphInfoMapper.selectList(graphInfoLambdaQueryWrapperApartment);
        ArrayList<GraphVo> graphVoApartments = new ArrayList<>();
        for (GraphInfo graphInfo : graphInfoListApartment) {
            graphVoApartments.add(new GraphVo(graphInfo.getName(), graphInfo.getUrl()));
        }
        //获取公寓最小租价
        BigDecimal minRent= roomInfoMapper.selectMinRentByApartmentId(apartmentInfo.getId());
        //获取公寓配套信息列表
        List<FacilityInfo> facilityInfoList= apartmentInfoMapper.selectFacilityInfoListByApartmentId(id);
        //组装公寓详情VO
        ApartmentDetailVo apartmentDetailVo = new ApartmentDetailVo();
        BeanUtils.copyProperties(apartmentInfo, apartmentDetailVo);
        apartmentDetailVo.setGraphVoList(graphVoApartments);
        apartmentDetailVo.setLabelInfoList(labelInfoListApartment);
        apartmentDetailVo.setMinRent(minRent);
        apartmentDetailVo.setFacilityInfoList(facilityInfoList);
        return apartmentDetailVo;
    }
}




