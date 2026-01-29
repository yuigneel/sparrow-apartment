package com.yulgnier.web.admin.controller.apartment;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yulgnier.common.result.Result;
import com.yulgnier.model.entity.CityInfo;
import com.yulgnier.model.entity.DistrictInfo;
import com.yulgnier.model.entity.ProvinceInfo;
import com.yulgnier.web.admin.service.CityInfoService;
import com.yulgnier.web.admin.service.DistrictInfoService;
import com.yulgnier.web.admin.service.ProvinceInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "地区信息管理")
@RestController
@RequestMapping("/admin/region")
public class RegionInfoController {

    private final ProvinceInfoService provinceInfoService;
    private final CityInfoService cityInfoService;
    private final DistrictInfoService districtInfoService;

    // 构造函数注入
    @Autowired      // @Autowired注解，告诉Spring容器，将这个对象注入到这个类中，在spring4.3版本后，当只有一个构造函数时，@Autowired可以省略
    public RegionInfoController(ProvinceInfoService provinceInfoService,
                                CityInfoService cityInfoService,
                                DistrictInfoService districtInfoService) {
        this.provinceInfoService = provinceInfoService;
        this.cityInfoService = cityInfoService;
        this.districtInfoService = districtInfoService;
    }

    @Operation(summary = "查询省份信息列表")
    @GetMapping("province/list")
    public Result<List<ProvinceInfo>> listProvince() {
        List<ProvinceInfo> list = provinceInfoService.list();
        return Result.ok(list);
    }

    @Operation(summary = "根据省份id查询城市信息列表")
    @GetMapping("city/listByProvinceId")
    public Result<List<CityInfo>> listCityInfoByProvinceId(@RequestParam Long id) {
        LambdaQueryWrapper<CityInfo> cityInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        cityInfoLambdaQueryWrapper.eq(CityInfo::getProvinceId, id);
        List<CityInfo> list = cityInfoService.list(cityInfoLambdaQueryWrapper);
        return Result.ok(list);
    }

    @GetMapping("district/listByCityId")
    @Operation(summary = "根据城市id查询区县信息")
    public Result<List<DistrictInfo>> listDistrictInfoByCityId(@RequestParam Long id) {
        LambdaQueryWrapper<DistrictInfo> districtInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        districtInfoLambdaQueryWrapper.eq(DistrictInfo::getCityId, id);
        List<DistrictInfo> list = districtInfoService.list(districtInfoLambdaQueryWrapper);
        return Result.ok(list);
    }
}
