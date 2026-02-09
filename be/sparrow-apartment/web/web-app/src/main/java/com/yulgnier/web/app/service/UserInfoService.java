package com.yulgnier.web.app.service;

import com.yulgnier.model.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yulgnier.web.app.vo.user.UserInfoVo;

/**
* @author liubo
* @description 针对表【user_info(用户信息表)】的数据库操作Service
* @createDate 2023-07-26 11:12:39
*/
public interface UserInfoService extends IService<UserInfo> {
    UserInfoVo getUserInfoVoById(Long userId);
}