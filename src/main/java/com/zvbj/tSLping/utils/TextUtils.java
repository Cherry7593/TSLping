package com.zvbj.tSLping.utils;

import com.zvbj.tSLping.config.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class TextUtils {

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
        Component pageInfo = colorize(" &7| &e第 " + currentPage + "/" + totalPages + " 页 &7| ");
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
}
