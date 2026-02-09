package com.yulgnier.web.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yulgnier.common.constant.RedisConstant;
import com.yulgnier.common.exception.LeaseException;
import com.yulgnier.common.result.ResultCodeEnum;
import com.yulgnier.common.utils.CodeUtil;
import com.yulgnier.common.utils.JwtUtil;
import com.yulgnier.model.enmu.BaseStatus;
import com.yulgnier.model.entity.UserInfo;
import com.yulgnier.web.app.service.LoginService;
import com.yulgnier.web.app.service.SmsService;

import com.yulgnier.web.app.service.UserInfoService;
import com.yulgnier.web.app.vo.user.LoginVo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {
    private final SmsService smsService;
    private final StringRedisTemplate stringRedisTemplate;
    private final UserInfoService userInfoService;

    public LoginServiceImpl(SmsService smsService, StringRedisTemplate stringRedisTemplate, UserInfoService userInfoService) {
        this.smsService = smsService;
        this.stringRedisTemplate = stringRedisTemplate;
        this.userInfoService = userInfoService;
    }

    @Override
    public void getCode(String phone) {
        String randomCode = CodeUtil.getRandomCode(6);
        String key = RedisConstant.APP_LOGIN_PREFIX + phone;
        if (stringRedisTemplate.hasKey(key)) {
            Long ttl = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
            if (RedisConstant.APP_LOGIN_CODE_TTL_SEC - ttl < RedisConstant.APP_LOGIN_CODE_RESEND_TIME_SEC) {
                throw new LeaseException(ResultCodeEnum.APP_SEND_SMS_TOO_OFTEN);
            }
        }
        smsService.sendCode(phone, randomCode);
        stringRedisTemplate.opsForValue().set(key, randomCode, RedisConstant.APP_LOGIN_CODE_TTL_SEC, TimeUnit.SECONDS);
    }

    @Override
    public String login(LoginVo loginVo) {
        if (loginVo.getPhone() == null) {
            throw new LeaseException(ResultCodeEnum.APP_LOGIN_PHONE_EMPTY);
        }
        if (loginVo.getCode() == null) {
            throw new LeaseException(ResultCodeEnum.APP_LOGIN_CODE_EMPTY);
        }
        //这里的验证码因为国内无法发送，所以有个跳过功能，输入六个零直接算验证成功
        if (!"000000".equals(loginVo.getCode())) {
            String key = RedisConstant.APP_LOGIN_PREFIX + loginVo.getPhone();
            String code = stringRedisTemplate.opsForValue().get(key);
            if (!loginVo.getCode().equals(code)) {
                throw new LeaseException(ResultCodeEnum.APP_LOGIN_CODE_ERROR);
            }
        }
        //开始验证用户是否存在
        LambdaQueryWrapper<UserInfo> userInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userInfoLambdaQueryWrapper.eq(UserInfo::getPhone, loginVo.getPhone());
        UserInfo userInfo = userInfoService.getOne(userInfoLambdaQueryWrapper);
        if (userInfo == null) {
            userInfo = new UserInfo();
            userInfo.setPhone(loginVo.getPhone());
            userInfo.setStatus(BaseStatus.ENABLE);
            userInfo.setNickname("用户-" + loginVo.getPhone().substring(7));
            userInfoService.save(userInfo);
        }
        if (userInfo.getStatus() == BaseStatus.DISABLE) {
            throw new LeaseException(ResultCodeEnum.APP_ACCOUNT_DISABLED_ERROR);
        }
        return JwtUtil.createToken(userInfo.getId(), userInfo.getPhone());
    }
}
