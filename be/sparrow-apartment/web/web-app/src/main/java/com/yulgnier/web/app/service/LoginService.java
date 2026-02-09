package com.yulgnier.web.app.service;

import com.yulgnier.web.app.vo.user.LoginVo;

public interface LoginService {
    void getCode(String phone);

    String login(LoginVo loginVo);
}
