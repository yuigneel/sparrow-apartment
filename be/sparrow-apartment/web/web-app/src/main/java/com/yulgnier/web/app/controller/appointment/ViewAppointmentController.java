package com.yulgnier.web.app.controller.appointment;


import com.yulgnier.common.login.LoginUser;
import com.yulgnier.common.login.LoginUserHolder;
import com.yulgnier.common.result.Result;
import com.yulgnier.model.entity.ViewAppointment;
import com.yulgnier.web.app.service.ViewAppointmentService;
import com.yulgnier.web.app.vo.appointment.AppointmentDetailVo;
import com.yulgnier.web.app.vo.appointment.AppointmentItemVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "看房预约信息")
@RestController
@RequestMapping("/app/appointment")
public class ViewAppointmentController {

    private final ViewAppointmentService viewAppointmentService;

    public ViewAppointmentController(ViewAppointmentService viewAppointmentService) {
        this.viewAppointmentService = viewAppointmentService;
    }

    @Operation(summary = "保存或更新看房预约")
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody ViewAppointment viewAppointment) {
        LoginUser loginUser = LoginUserHolder.getLoginUser();
        viewAppointment.setUserId(loginUser.getUserId());
        viewAppointment.setName(loginUser.getUsername());
        viewAppointmentService.saveOrUpdate(viewAppointment);
        return Result.ok();
    }

    @Operation(summary = "查询个人预约看房列表")
    @GetMapping("listItem")
    public Result<List<AppointmentItemVo>> listItem() {
        List<AppointmentItemVo> result = viewAppointmentService.getAppointmentItemVoByUserId(LoginUserHolder.getLoginUser().getUserId());
        return Result.ok(result);
    }

    @GetMapping("getDetailById")
    @Operation(summary = "根据ID查询预约详情信息")
    public Result<AppointmentDetailVo> getDetailById(Long id) {
        AppointmentDetailVo appointmentDetailVo = viewAppointmentService.getAppointmentDetailVoById(id);
        return Result.ok(appointmentDetailVo);
    }
}

