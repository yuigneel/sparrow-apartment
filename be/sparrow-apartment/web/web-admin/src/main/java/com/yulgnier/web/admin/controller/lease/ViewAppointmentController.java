package com.yulgnier.web.admin.controller.lease;



import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yulgnier.common.result.Result;
import com.yulgnier.model.enmu.AppointmentStatus;
import com.yulgnier.model.entity.ViewAppointment;
import com.yulgnier.web.admin.service.ViewAppointmentService;
import com.yulgnier.web.admin.vo.appointment.AppointmentQueryVo;
import com.yulgnier.web.admin.vo.appointment.AppointmentVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

@Tag(name = "预约看房管理")
@RequestMapping("/admin/appointment")
@RestController
public class ViewAppointmentController {

    private final ViewAppointmentService viewAppointmentService;

    public ViewAppointmentController(ViewAppointmentService viewAppointmentService) {
        this.viewAppointmentService = viewAppointmentService;
    }

    @Operation(summary = "分页查询预约信息")
    @GetMapping("page")
    public Result<IPage<AppointmentVo>> page(@RequestParam long current, @RequestParam long size, AppointmentQueryVo queryVo) {
        Page<AppointmentVo> page = new Page<>(current, size);
        IPage<AppointmentVo> appointmentVoIPage = viewAppointmentService.pageAppointmentVo(page, queryVo);
        return Result.ok(appointmentVoIPage);
    }

    @Operation(summary = "根据id更新预约状态")
    @PostMapping("updateStatusById")
    public Result updateStatusById(@RequestParam Long id, @RequestParam AppointmentStatus status) {
        LambdaUpdateWrapper<ViewAppointment> viewAppointmentLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        viewAppointmentLambdaUpdateWrapper.eq(ViewAppointment::getId, id);
        viewAppointmentLambdaUpdateWrapper.set(ViewAppointment::getAppointmentStatus, status);
        viewAppointmentService.update(viewAppointmentLambdaUpdateWrapper);
        return Result.ok();
    }

}
