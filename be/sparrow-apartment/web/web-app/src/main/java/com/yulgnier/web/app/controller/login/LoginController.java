package com.yulgnier.web.app.controller.login;


import com.yulgnier.common.login.LoginUserHolder;
import com.yulgnier.common.result.Result;
import com.yulgnier.web.app.service.LoginService;
import com.yulgnier.web.app.service.UserInfoService;
import com.yulgnier.web.app.vo.user.LoginVo;
import com.yulgnier.web.app.vo.user.UserInfoVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "登录管理")
@RestController
@RequestMapping("/app/")
public class LoginController {
    private final LoginService loginService;
    private final UserInfoService userInfoService;

    public LoginController(LoginService loginService, UserInfoService userInfoService) {
        this.loginService = loginService;
        this.userInfoService = userInfoService;
    }

    @GetMapping("login/getCode")
    @Operation(summary = "获取短信验证码")
    public Result getCode(@RequestParam String phone) {
        loginService.getCode(phone);
        return Result.ok();
    }

    @PostMapping("login")
    @Operation(summary = "登录")
    public Result<String> login(@RequestBody LoginVo loginVo) {
        String token = loginService.login(loginVo);
        return Result.ok(token);
    }

    @GetMapping("info")
    @Operation(summary = "获取登录用户信息")
    public Result<UserInfoVo> info() {
        Long userId = LoginUserHolder.getLoginUser().getUserId();
        UserInfoVo userInfoVo = userInfoService.getUserInfoVoById(userId);
        return Result.ok(userInfoVo);
    }
}

