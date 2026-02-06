package com.yulgnier.web.admin.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wf.captcha.SpecCaptcha;
import com.yulgnier.common.constant.RedisConstant;
import com.yulgnier.common.exception.LeaseException;
import com.yulgnier.common.result.ResultCodeEnum;
import com.yulgnier.common.utils.JwtUtil;
import com.yulgnier.model.enmu.BaseStatus;
import com.yulgnier.model.entity.SystemUser;
import com.yulgnier.web.admin.mapper.SystemUserMapper;
import com.yulgnier.web.admin.service.LoginService;
import com.yulgnier.web.admin.vo.login.CaptchaVo;
import com.yulgnier.web.admin.vo.login.LoginVo;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    private final StringRedisTemplate stringRedisTemplate;
    private final SystemUserMapper systemUserMapper;

    public LoginServiceImpl(StringRedisTemplate stringRedisTemplate, SystemUserMapper systemUserMapper) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.systemUserMapper = systemUserMapper;
    }

    @Override
    public CaptchaVo getCaptcha() {
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 4);
        // 生成key，value
        String code = specCaptcha.text().toLowerCase();   // 不区分大小写
        String key = RedisConstant.ADMIN_LOGIN_PREFIX + UUID.randomUUID();
        //存入 Redis
        stringRedisTemplate.opsForValue().set(key, code,RedisConstant.ADMIN_LOGIN_CAPTCHA_TTL_SEC, TimeUnit.MINUTES);
        //返回
        return new CaptchaVo(specCaptcha.toBase64(), key);
    }

    @Override
    public String login(LoginVo loginVo) {
        if (loginVo.getCaptchaCode() == null){
            throw new LeaseException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_NOT_FOUND);
        }

        String code = stringRedisTemplate.opsForValue().get(loginVo.getCaptchaKey());

        if (code == null){
            throw new LeaseException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_EXPIRED);
        }
        if (!code.equals(loginVo.getCaptchaCode().toLowerCase())){
            throw new LeaseException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_ERROR);
        }
        LambdaQueryWrapper<SystemUser> systemUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        systemUserLambdaQueryWrapper.eq(SystemUser::getUsername, loginVo.getUsername());
        SystemUser systemUser = systemUserMapper.selectOne(systemUserLambdaQueryWrapper);

        if (systemUser == null){
            throw new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_NOT_EXIST_ERROR);
        }

        if (!systemUser.getPassword().equals(DigestUtils.md5Hex(loginVo.getPassword()))){
            throw new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_ERROR);
        }

        if (systemUser.getStatus()== BaseStatus.DISABLE){
            throw new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_DISABLED_ERROR);
        }
        return JwtUtil.createToken(systemUser.getId(), systemUser.getUsername());
    }
}
