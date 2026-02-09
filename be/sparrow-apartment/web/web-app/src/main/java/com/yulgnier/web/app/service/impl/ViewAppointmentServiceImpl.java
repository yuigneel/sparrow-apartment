package com.yulgnier.web.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yulgnier.model.entity.ApartmentInfo;
import com.yulgnier.model.entity.RoomInfo;
import com.yulgnier.model.entity.ViewAppointment;
import com.yulgnier.web.app.mapper.ApartmentInfoMapper;
import com.yulgnier.web.app.mapper.ViewAppointmentMapper;
import com.yulgnier.web.app.service.ViewAppointmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yulgnier.web.app.vo.apartment.ApartmentItemVo;
import com.yulgnier.web.app.vo.appointment.AppointmentDetailVo;
import com.yulgnier.web.app.vo.appointment.AppointmentItemVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liubo
 * @description 针对表【view_appointment(预约看房信息表)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
public class ViewAppointmentServiceImpl extends ServiceImpl<ViewAppointmentMapper, ViewAppointment>
        implements ViewAppointmentService {
    private final ViewAppointmentMapper viewAppointmentMapper;
    private final ApartmentInfoMapper apartmentInfoMapper;


    public ViewAppointmentServiceImpl(ViewAppointmentMapper viewAppointmentMapper, ApartmentInfoMapper apartmentInfoMapper) {
        this.viewAppointmentMapper = viewAppointmentMapper;

        this.apartmentInfoMapper = apartmentInfoMapper;
    }

    @Override
    public List<AppointmentItemVo> getAppointmentItemVoByUserId(Long userId) {
        return viewAppointmentMapper.getAppointmentItemVoByUserId(userId);
    }

    @Override
    public AppointmentDetailVo getAppointmentDetailVoById(Long id) {
        // 先获取预约信息
        ViewAppointment viewAppointment = viewAppointmentMapper.selectById(id);
        // 再获取公寓基本信息
        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectById(viewAppointment.getApartmentId());
        // 再获取公寓Vo的其它信息
        ApartmentItemVo apartmentItemVo = apartmentInfoMapper.selectApartmentItemVoById(apartmentInfo.getId());
        // 组装完整的公寓Vo
        BeanUtils.copyProperties(apartmentInfo, apartmentItemVo);
        // 组装完整的预约信息VO
        AppointmentDetailVo appointmentDetailVo = new AppointmentDetailVo();
        BeanUtils.copyProperties(viewAppointment, appointmentDetailVo);
        appointmentDetailVo.setApartmentItemVo(apartmentItemVo);
        return appointmentDetailVo;
    }
}




