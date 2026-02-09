package com.yulgnier.web.app.service.impl;

import com.yulgnier.model.entity.LeaseAgreement;
import com.yulgnier.web.app.mapper.LeaseAgreementMapper;
import com.yulgnier.web.app.service.LeaseAgreementService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yulgnier.web.app.vo.agreement.AgreementDetailVo;
import com.yulgnier.web.app.vo.agreement.AgreementItemVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liubo
 * @description 针对表【lease_agreement(租约信息表)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
public class LeaseAgreementServiceImpl extends ServiceImpl<LeaseAgreementMapper, LeaseAgreement>
        implements LeaseAgreementService {
    private final LeaseAgreementMapper leaseAgreementMapper;

    public LeaseAgreementServiceImpl(LeaseAgreementMapper leaseAgreementMapper) {
        this.leaseAgreementMapper = leaseAgreementMapper;
    }

    @Override
    public List<AgreementItemVo> listItemByPhone(String username) {
        return leaseAgreementMapper.listItemByPhone(username);
    }

    @Override
    public AgreementDetailVo getDetailById(Long id) {
        return leaseAgreementMapper.getDetailById(id);
    }
}




