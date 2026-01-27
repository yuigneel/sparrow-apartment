package com.yulgnier.web.admin.vo.fee;


import com.yulgnier.model.entity.FeeKey;
import com.yulgnier.model.entity.FeeValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class FeeKeyVo extends FeeKey {

    @Schema(description = "杂费value列表")
    private List<FeeValue> feeValueList;
}
