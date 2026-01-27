package com.yulgnier.web.admin.vo.appointment;


import com.yulgnier.model.entity.ApartmentInfo;
import com.yulgnier.model.entity.ViewAppointment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "预约看房信息")
public class AppointmentVo extends ViewAppointment {

    @Schema(description = "预约公寓信息")
    private ApartmentInfo apartmentInfo;

}
