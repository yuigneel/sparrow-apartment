package com.yulgnier.web.admin.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yulgnier.model.enmu.ItemType;
import com.yulgnier.model.entity.GraphInfo;
import com.yulgnier.web.admin.mapper.GraphInfoMapper;
import com.yulgnier.web.admin.service.GraphInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author liubo
* @description 针对表【graph_info(图片信息表)】的数据库操作Service实现
* @createDate 2023-07-24 15:48:00
*/
@Service
public class GraphInfoServiceImpl extends ServiceImpl<GraphInfoMapper, GraphInfo>
    implements GraphInfoService {
}




