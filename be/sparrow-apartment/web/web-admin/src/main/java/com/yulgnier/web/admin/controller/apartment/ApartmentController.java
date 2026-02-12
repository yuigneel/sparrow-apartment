package com.yulgnier.web.admin.controller.apartment;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yulgnier.common.result.Result;
import com.yulgnier.model.enmu.ReleaseStatus;
import com.yulgnier.model.entity.ApartmentInfo;
import com.yulgnier.web.admin.service.ApartmentInfoService;
import com.yulgnier.web.admin.vo.apartment.ApartmentDetailVo;
import com.yulgnier.web.admin.vo.apartment.ApartmentItemVo;
import com.yulgnier.web.admin.vo.apartment.ApartmentQueryVo;
import com.yulgnier.web.admin.vo.apartment.ApartmentSubmitVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "公寓信息管理")
@RestController
@RequestMapping("/admin/apartment")
public class ApartmentController {

    // 使用 Lombok @Slf4j 自动生成的 log 字段

    @Autowired
    private ApartmentInfoService apartmentInfoService;

    @Operation(summary = "保存或更新公寓信息")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody ApartmentSubmitVo apartmentSubmitVo) {
        apartmentInfoService.saveOrUpdateApartment(apartmentSubmitVo);
        return Result.ok();
    }

    @Operation(summary = "根据条件分页查询公寓列表")
    @GetMapping("pageItem")
    public Result<IPage<ApartmentItemVo>> pageItem(@RequestParam long current, @RequestParam long size, ApartmentQueryVo queryVo) {
        // 打印入参：确认current/size/queryVo是否符合预期
        log.info("分页入参：current={}, size={}, queryVo={}", current, size, queryVo);
        Page<ApartmentItemVo> apartmentItemVoPage = new Page<>(current, size);
        IPage<ApartmentItemVo> result = apartmentInfoService.pageItem(apartmentItemVoPage, queryVo);
        // 打印出参：确认总条数、数据列表是否为空
        log.info("分页出参：总条数={}, 总页数={}, 数据列表={}", result.getTotal(), result.getPages(), result.getRecords());
        return Result.ok(result);
    }

    @Operation(summary = "根据ID获取公寓详细信息")
    @GetMapping("getDetailById")
    public Result<ApartmentDetailVo> getDetailById(@RequestParam Long id) {
        ApartmentDetailVo apartmentDetailVo = apartmentInfoService.getDetailById(id);
        return Result.ok(apartmentDetailVo);
    }

    @Operation(summary = "根据id删除公寓信息")
    @DeleteMapping("removeById")
    public Result removeById(@RequestParam Long id) {
        apartmentInfoService.removeApartmentById(id);
        return Result.ok();
    }

    @Operation(summary = "根据id修改公寓发布状态")
    @PostMapping("updateReleaseStatusById")
    public Result updateReleaseStatusById(@RequestParam Long id, @RequestParam ReleaseStatus status) {
        LambdaUpdateWrapper<ApartmentInfo> apartmentInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        apartmentInfoLambdaUpdateWrapper.eq(ApartmentInfo::getId, id);
        apartmentInfoLambdaUpdateWrapper.eq(ApartmentInfo::getIsRelease, status);
        apartmentInfoService.update(apartmentInfoLambdaUpdateWrapper);
        return Result.ok();
    }

    @Operation(summary = "根据区县id查询公寓信息列表")
    @GetMapping("listInfoByDistrictId")
    public Result<List<ApartmentInfo>> listInfoByDistrictId(@RequestParam Long id) {
        LambdaQueryWrapper<ApartmentInfo> apartmentInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        apartmentInfoLambdaQueryWrapper.eq(ApartmentInfo::getDistrictId, id);
        List<ApartmentInfo> apartmentInfoList = apartmentInfoService.list(apartmentInfoLambdaQueryWrapper);
        return Result.ok(apartmentInfoList);
    }
}














