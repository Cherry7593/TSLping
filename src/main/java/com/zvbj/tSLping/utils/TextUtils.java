package com.zvbj.tSLping.utils;

import com.zvbj.tSLping.config.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class TextUtils {

    // 设置固定宽度用于对齐
    private static final int PLAYER_NAME_WIDTH = 16;
    private static final int RANK_WIDTH = 4;

    public static Component colorize(String text) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(text);
    }

    public static Component getPingComponent(String playerName, int ping, ConfigManager config) {
        TextColor color = getPingColor(ping, config);
        String format = config.getMessage("ping_format")
                .replace("{player}", playerName)
                .replace("{ping}", String.valueOf(ping));

        return LegacyComponentSerializer.legacyAmpersand().deserialize(format)
                .colorIfAbsent(color);
    }

    /**
     * 创建格式化的ping条目，支持对齐显示
     */
    public static Component createFormattedPingEntry(int rank, String playerName, int ping, ConfigManager config) {
        // 格式化排名，���对齐，使用灰色，序号和点之间无空格
        String formattedRank = rank + ".";

        // 格式化玩家名称，左对齐并填充空格，保持白色
        String paddedPlayerName = String.format("%-" + PLAYER_NAME_WIDTH + "s", playerName);

        // 获取带颜色的ping显示
        String coloredPing = getFormattedPing(ping, config);

        // 创建带颜色的组件
        Component rankComponent = colorize("&7" + formattedRank); // 灰色序号
        Component nameComponent = colorize("&f" + paddedPlayerName); // 白色玩家名
        Component pingComponent = colorize(coloredPing); // 带颜色的延迟

        // 组合显示文本，更紧凑的间距
        return rankComponent.append(nameComponent).append(Component.text("     ")).append(pingComponent);
    }

    public static TextColor getPingColor(int ping, ConfigManager config) {
        if (ping < config.getGreenThreshold()) {
            return NamedTextColor.GREEN;
        } else if (ping < config.getYellowThreshold()) {
            return NamedTextColor.YELLOW;
        } else {
            return NamedTextColor.RED;
        }
    }

    public static Component createClickableButton(String text, String command, String hoverText) {
        return colorize(text)
                .clickEvent(ClickEvent.runCommand(command))
                .hoverEvent(HoverEvent.showText(colorize(hoverText)));
    }

    public static Component createPaginationButtons(int currentPage, int totalPages, ConfigManager config) {
        Component result = Component.empty();

        // 添加上一页按钮
        if (currentPage > 1) {
            Component prevButton = createClickableButton(
                config.getPreviousButton(),
                "/tping all " + (currentPage - 1),
                config.getPreviousHover()
            );
            result = result.append(prevButton);
        }

        // 添加当前页信息
        Component pageInfo = colorize(" | 第 " + currentPage + "/" + totalPages + " 页 | ");
        result = result.append(pageInfo);

        // 添加下一页按钮
        if (currentPage < totalPages) {
            Component nextButton = createClickableButton(
                config.getNextButton(),
                "/tping all " + (currentPage + 1),
                config.getNextHover()
            );
            result = result.append(nextButton);
        }

        return result;
    }

    /**
     * 获取格式化的ping显示文本（带颜色）
     */
    public static String getFormattedPing(int ping, ConfigManager config) {
        String colorCode;
        if (ping < config.getGreenThreshold()) {
            colorCode = "&a"; // 绿色
        } else if (ping < config.getYellowThreshold()) {
            colorCode = "&e"; // 黄色
        } else {
            colorCode = "&c"; // 红色
        }
        return colorCode + ping + "ms";
    }

    /**
     * 获取格式化的平均延迟显示文本（带颜色）
     */
    public static String getFormattedAveragePing(double averagePing, ConfigManager config) {
        String colorCode;
        int ping = (int) Math.round(averagePing);

        if (ping < config.getGreenThreshold()) {
            colorCode = "&a"; // 绿色
        } else if (ping < config.getYellowThreshold()) {
            colorCode = "&e"; // 黄色
        } else {
            colorCode = "&c"; // 红色
        }

        // 直接返回带颜色的延迟显示，紧跟在冒号后面
        return colorCode + String.format("%.1f", averagePing) + "ms";
    }
}
