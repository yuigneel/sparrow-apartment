package com.yulgnier.web.app.controller.agreement;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yulgnier.common.login.LoginUserHolder;
import com.yulgnier.common.result.Result;
import com.yulgnier.model.entity.LeaseAgreement;
import com.yulgnier.model.enmu.LeaseStatus;
import com.yulgnier.web.app.service.LeaseAgreementService;
import com.yulgnier.web.app.vo.agreement.AgreementDetailVo;
import com.yulgnier.web.app.vo.agreement.AgreementItemVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/agreement")
@Tag(name = "租约信息")
public class LeaseAgreementController {

    private final LeaseAgreementService leaseAgreementService;

    public LeaseAgreementController(LeaseAgreementService leaseAgreementService) {
        this.leaseAgreementService = leaseAgreementService;
    }

    @Operation(summary = "获取个人租约基本信息列表")
    @GetMapping("listItem")
    public Result<List<AgreementItemVo>> listItem() {
        List<AgreementItemVo> result = leaseAgreementService.listItemByPhone(LoginUserHolder.getLoginUser().getUsername());
        return Result.ok(result);
    }

    @Operation(summary = "根据id获取租约详细信息")
    @GetMapping("getDetailById")
    public Result<AgreementDetailVo> getDetailById(@RequestParam Long id) {
        AgreementDetailVo result = leaseAgreementService.getDetailById(id);
        return Result.ok(result);
    }

    @Operation(summary = "根据id更新租约状态", description = "用于确认租约和提前退租")
    @PostMapping("updateStatusById")
    public Result updateStatusById(@RequestParam Long id, @RequestParam LeaseStatus leaseStatus) {
        LambdaUpdateWrapper<LeaseAgreement> leaseAgreementLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        leaseAgreementLambdaUpdateWrapper.eq(LeaseAgreement::getId, id).set(LeaseAgreement::getStatus, leaseStatus);
        leaseAgreementService.update(leaseAgreementLambdaUpdateWrapper);
        return Result.ok();
    }

    @Operation(summary = "保存或更新租约", description = "用于续约")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody LeaseAgreement leaseAgreement) {
        leaseAgreementService.saveOrUpdate(leaseAgreement);
        return Result.ok();
    }

}
