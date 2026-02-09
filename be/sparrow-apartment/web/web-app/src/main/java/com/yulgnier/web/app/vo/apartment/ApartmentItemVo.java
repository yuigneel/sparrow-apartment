package com.yulgnier.web.app.vo.apartment;


import com.yulgnier.model.entity.ApartmentInfo;
import com.yulgnier.model.entity.LabelInfo;
import com.yulgnier.web.app.vo.graph.GraphVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "App端公寓信息")
public class ApartmentItemVo extends ApartmentInfo {

    private List<LabelInfo> labelInfoList;  //标签

    private List<GraphVo> graphVoList; //图片

    private BigDecimal minRent; //最小租价
}
