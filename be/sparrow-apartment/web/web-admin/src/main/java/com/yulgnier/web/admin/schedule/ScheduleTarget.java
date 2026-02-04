package com.yulgnier.web.admin.schedule;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yulgnier.model.enmu.LeaseStatus;
import com.yulgnier.model.entity.LeaseAgreement;
import com.yulgnier.web.admin.service.LeaseAgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.time.LocalDate;

@Component
public class ScheduleTarget {

    private final LeaseAgreementService leaseAgreementService;

    public ScheduleTarget(LeaseAgreementService leaseAgreementService) {
        this.leaseAgreementService = leaseAgreementService;
    }

    //每天检查处理租约状态一次
    @Scheduled(cron = "0 0 0 * * *")
    public void checkLeaseAgreement() {
        System.out.println("=======================开始检查租约信息====================");
        LambdaUpdateWrapper<LeaseAgreement> leaseAgreementLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        leaseAgreementLambdaUpdateWrapper.le(LeaseAgreement::getLeaseEndDate, LocalDate.now());
        leaseAgreementLambdaUpdateWrapper.in(LeaseAgreement::getStatus,LeaseStatus.SIGNED,LeaseStatus.WITHDRAWING);
        leaseAgreementLambdaUpdateWrapper.set(LeaseAgreement::getStatus, LeaseStatus.EXPIRED);
        leaseAgreementService.update(leaseAgreementLambdaUpdateWrapper);
        System.out.println("=======================检查完成====================");
    }
}
