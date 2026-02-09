package com.yulgnier.web.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yulgnier.model.entity.BrowsingHistory;
import com.yulgnier.web.app.mapper.BrowsingHistoryMapper;
import com.yulgnier.web.app.service.BrowsingHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yulgnier.web.app.vo.history.HistoryItemVo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author liubo
 * @description 针对表【browsing_history(浏览历史)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
public class BrowsingHistoryServiceImpl extends ServiceImpl<BrowsingHistoryMapper, BrowsingHistory>
        implements BrowsingHistoryService {
    private final BrowsingHistoryMapper browsingHistoryMapper;

    public BrowsingHistoryServiceImpl(BrowsingHistoryMapper browsingHistoryMapper) {
        this.browsingHistoryMapper = browsingHistoryMapper;
    }

    @Override
    public IPage<HistoryItemVo> pageHistoryItemVoByUserId(Page<HistoryItemVo> historyItemVoPage, Long userId) {
        return browsingHistoryMapper.pageHistoryItemVoByUserId(historyItemVoPage, userId);
    }

    @Override
    @Async // 异步方法, 开启新的线程执行 l……g……n……i……e……r：得加钱
    public void saveHistory(Long userId, Long id) {
        LambdaQueryWrapper<BrowsingHistory> browsingHistoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        browsingHistoryLambdaQueryWrapper.eq(BrowsingHistory::getUserId, userId)
                .eq(BrowsingHistory::getRoomId, id);
        BrowsingHistory browsingHistory = browsingHistoryMapper.selectOne(browsingHistoryLambdaQueryWrapper);
        if (browsingHistory != null) {
            browsingHistory.setBrowseTime(new Date());
            browsingHistoryMapper.updateById(browsingHistory);
        } else {
            browsingHistory = new BrowsingHistory();
            browsingHistory.setUserId(userId);
            browsingHistory.setRoomId(id);
            browsingHistory.setBrowseTime(new Date());
            browsingHistoryMapper.insert(browsingHistory);
        }
    }
}