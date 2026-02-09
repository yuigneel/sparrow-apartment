package com.yulgnier.web.app.controller.history;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yulgnier.common.login.LoginUserHolder;
import com.yulgnier.common.result.Result;
import com.yulgnier.web.app.service.BrowsingHistoryService;
import com.yulgnier.web.app.vo.history.HistoryItemVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "浏览历史管理")
@RequestMapping("/app/history")
public class BrowsingHistoryController {
private final BrowsingHistoryService browsingHistoryService;

    public BrowsingHistoryController(BrowsingHistoryService browsingHistoryService) {
        this.browsingHistoryService = browsingHistoryService;
    }

    @Operation(summary = "获取浏览历史")
    @GetMapping("pageItem")
    private Result<IPage<HistoryItemVo>> page(@RequestParam long current, @RequestParam long size) {
        Page<HistoryItemVo> historyItemVoPage = new Page<>(current, size);
        Long userId = LoginUserHolder.getLoginUser().getUserId();
        IPage<HistoryItemVo> result = browsingHistoryService.pageHistoryItemVoByUserId(historyItemVoPage, userId);
        return Result.ok(result);
    }
}
