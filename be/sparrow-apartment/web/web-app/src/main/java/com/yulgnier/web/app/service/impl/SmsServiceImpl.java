package com.yulgnier.web.app.service.impl;

import com.yulgnier.web.app.service.SmsService;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {
    @Override
    public void sendCode(String phone, String code) {
        //这里模拟发送短信
        System.out.println("===============已向手机号" + phone + "发送验证码：" + code+"====================");
    }
}
