package com.yulgnier.web.admin.controller.user;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yulgnier.common.result.Result;
import com.yulgnier.model.enmu.BaseStatus;
import com.yulgnier.model.entity.UserInfo;
import com.yulgnier.web.admin.service.UserInfoService;
import com.yulgnier.web.admin.vo.user.UserInfoQueryVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户信息管理")
@RestController
@RequestMapping("/admin/user")
public class UserInfoController {

    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Operation(summary = "分页查询用户信息")
    @GetMapping("page")
    public Result<IPage<UserInfo>> pageUserInfo(@RequestParam long current, @RequestParam long size, UserInfoQueryVo queryVo) {
        Page<UserInfo> userInfoPage = new Page<>(current, size);
        LambdaQueryWrapper<UserInfo> userInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userInfoLambdaQueryWrapper.like(queryVo.getPhone() != null, UserInfo::getPhone, queryVo.getPhone());
        userInfoLambdaQueryWrapper.eq(queryVo.getStatus() != null, UserInfo::getStatus, queryVo.getStatus());
        userInfoService.page(userInfoPage, userInfoLambdaQueryWrapper);
        return Result.ok(userInfoPage);
    }

    @Operation(summary = "根据用户id更新账号状态")
    @PostMapping("updateStatusById")
    public Result updateStatusById(@RequestParam Long id, @RequestParam BaseStatus status) {
        LambdaUpdateWrapper<UserInfo> userInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        userInfoLambdaUpdateWrapper.eq(UserInfo::getId, id);
        userInfoLambdaUpdateWrapper.set(UserInfo::getStatus, status);
        userInfoService.update(userInfoLambdaUpdateWrapper);
        return Result.ok();
    }
}
