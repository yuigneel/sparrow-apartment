package com.yulgnier.web.admin.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yulgnier.model.entity.*;
import com.yulgnier.web.admin.mapper.*;
import com.yulgnier.web.admin.service.LeaseAgreementService;
import com.yulgnier.web.admin.vo.agreement.AgreementQueryVo;
import com.yulgnier.web.admin.vo.agreement.AgreementVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liubo
 * @description 针对表【lease_agreement(租约信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class LeaseAgreementServiceImpl extends ServiceImpl<LeaseAgreementMapper, LeaseAgreement>
        implements LeaseAgreementService {

    private final LeaseAgreementMapper leaseAgreementMapper;
    private final ApartmentInfoMapper apartmentInfoMapper;
    private final RoomInfoMapper roomInfoMapper;
    private final PaymentTypeMapper paymentTypeMapper;
    private final LeaseTermMapper leaseTermMapper;

    public LeaseAgreementServiceImpl(LeaseAgreementMapper leaseAgreementMapper, ApartmentInfoMapper apartmentInfoMapper, RoomInfoMapper roomInfoMapper, PaymentTypeMapper paymentTypeMapper, LeaseTermMapper leaseTermMapper) {
        this.leaseAgreementMapper = leaseAgreementMapper;
        this.apartmentInfoMapper = apartmentInfoMapper;
        this.roomInfoMapper = roomInfoMapper;
        this.paymentTypeMapper = paymentTypeMapper;
        this.leaseTermMapper = leaseTermMapper;
    }

    @Override
    public AgreementVo getAgreementVoById(Long id) {
        LeaseAgreement leaseAgreement = leaseAgreementMapper.selectById(id);
        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectById(leaseAgreement.getApartmentId());
        RoomInfo roomInfo = roomInfoMapper.selectById(leaseAgreement.getRoomId());
        PaymentType paymentType = paymentTypeMapper.selectById(leaseAgreement.getPaymentTypeId());
        LeaseTerm leaseTerm = leaseTermMapper.selectById(leaseAgreement.getLeaseTermId());
        AgreementVo agreementVo = new AgreementVo();
        BeanUtils.copyProperties(leaseAgreement, agreementVo);
        agreementVo.setApartmentInfo(apartmentInfo);
        agreementVo.setRoomInfo(roomInfo);
        agreementVo.setPaymentType(paymentType);
        agreementVo.setLeaseTerm(leaseTerm);
        return agreementVo;
    }

    @Override
    public IPage<AgreementVo> pageAgreementVo(Page<AgreementVo> agreementVoPage, AgreementQueryVo queryVo) {
        return leaseAgreementMapper.pageAgreementVo(agreementVoPage, queryVo);
    }
}




