package com.zvbj.tSLping.pagination;

import com.zvbj.tSLping.config.ConfigManager;
import com.zvbj.tSLping.handlers.PingHandler;
import com.zvbj.tSLping.utils.TextUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

public class PingPaginator {
    private final ConfigManager config;

    public PingPaginator(ConfigManager config) {
        this.config = config;
    }

    /**
     * 显示分页的ping列表
     */
    public void showPingList(Player player, int page) {
        List<PingHandler.PlayerPingInfo> allPlayers = PingHandler.getAllPlayersPing();

        if (allPlayers.isEmpty()) {
            player.sendMessage(TextUtils.colorize(config.getMessage("no_players_online")));
            return;
        }

        int entriesPerPage = config.getEntriesPerPage();
        int totalPages = (int) Math.ceil((double) allPlayers.size() / entriesPerPage);

        // 验证页码
        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;

        // 显示分割线
        player.sendMessage(TextUtils.colorize(config.getSeparatorLine()));

        // 计算并显示服务器平均延迟（右对齐和玩家ping对齐）
        double averagePing = PingHandler.getAveragePing();
        String averagePingMessage = config.getAveragePingMessage(averagePing);
        player.sendMessage(TextUtils.colorize(averagePingMessage));

        // 计算当前页的数据范围
        int startIndex = (page - 1) * entriesPerPage;
        int endIndex = Math.min(startIndex + entriesPerPage, allPlayers.size());

        // 发送当前页的ping信息，使用新的格式化方法
        for (int i = startIndex; i < endIndex; i++) {
            PingHandler.PlayerPingInfo info = allPlayers.get(i);
            Component pingEntry = TextUtils.createFormattedPingEntry(i + 1, info.getPlayerName(), info.getPing(), config);
            player.sendMessage(pingEntry);
        }

        // 发送分页按钮
        if (totalPages > 1) {
            Component paginationButtons = TextUtils.createPaginationButtons(page, totalPages, config);
            player.sendMessage(paginationButtons);
        }
    }

    /**
     * 解析页码参数
     */
    public int parsePage(String[] args, int defaultPage) {
        if (args.length >= 2) {
            try {
                return Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                return defaultPage;
            }
        }
        return defaultPage;
    }
}
