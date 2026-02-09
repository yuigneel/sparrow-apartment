package com.yulgnier.web.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yulgnier.model.entity.UserInfo;
import com.yulgnier.web.app.service.UserInfoService;
import com.yulgnier.web.app.mapper.UserInfoMapper;
import com.yulgnier.web.app.vo.user.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author liubo
* @description 针对表【user_info(用户信息表)】的数据库操作Service实现
* @createDate 2023-07-26 11:12:39
*/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService{
private final UserInfoMapper userInfoMapper;

    public UserInfoServiceImpl(UserInfoMapper userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
    }

    @Override
    public UserInfoVo getUserInfoVoById(Long userId) {
        UserInfo userInfo = userInfoMapper.selectById(userId);
        return new UserInfoVo(userInfo.getNickname(), userInfo.getAvatarUrl());
    }
}




