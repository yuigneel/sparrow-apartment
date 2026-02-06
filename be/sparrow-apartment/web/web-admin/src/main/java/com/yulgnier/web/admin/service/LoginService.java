package com.yulgnier.web.admin.service;


import com.yulgnier.web.admin.vo.login.CaptchaVo;
import com.yulgnier.web.admin.vo.login.LoginVo;

public interface LoginService {

    CaptchaVo getCaptcha();

    String login(LoginVo loginVo);
}
