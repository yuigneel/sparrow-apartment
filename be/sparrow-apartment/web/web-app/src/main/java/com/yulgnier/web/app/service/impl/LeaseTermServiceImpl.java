package com.yulgnier.web.app.service.impl;

import com.yulgnier.model.entity.LeaseTerm;
import com.yulgnier.web.app.mapper.LeaseTermMapper;
import com.yulgnier.web.app.service.LeaseTermService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liubo
 * @description 针对表【lease_term(租期)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
public class LeaseTermServiceImpl extends ServiceImpl<LeaseTermMapper, LeaseTerm>
        implements LeaseTermService {
private final LeaseTermMapper leaseTermMapper;

    public LeaseTermServiceImpl(LeaseTermMapper leaseTermMapper) {
        this.leaseTermMapper = leaseTermMapper;
    }

    @Override
    public List<LeaseTerm> listLeaseTermByRoomId(Long id) {
        return leaseTermMapper.selectLeaseTermByRoomId(id);
    }
}




