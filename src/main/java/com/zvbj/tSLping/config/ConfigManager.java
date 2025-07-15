package com.zvbj.tSLping.config;

import com.zvbj.tSLping.TSLping;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private final TSLping plugin;
    private FileConfiguration config;

    public ConfigManager(TSLping plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    public void reloadConfig() {
        loadConfig();
    }

    public String getMessage(String key) {
        return config.getString("messages." + key, "&c配置项 " + key + " 未找到");
    }

    public String getMessageWithPlaceholder(String key, String placeholder, String value) {
        return getMessage(key).replace("{" + placeholder + "}", value);
    }

    // 重命名方法以避免重载冲突
    public String getFormattedMessage(String key, String player, String ping) {
        return getMessage(key)
                .replace("{player}", player)
                .replace("{ping}", ping);
    }

    // 添加通用的多参数替换方法
    public String getMessageWithReplacements(String key, String... replacements) {
        String message = getMessage(key);
        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 < replacements.length) {
                message = message.replace("{" + replacements[i] + "}", replacements[i + 1]);
            }
        }
        return message;
    }

    public String getPageHeader(int page, int totalPages) {
        return getMessage("page_header")
                .replace("{page}", String.valueOf(page))
                .replace("{total_pages}", String.valueOf(totalPages));
    }

    public int getEntriesPerPage() {
        return config.getInt("settings.entries_per_page", 10);
    }

    public int getGreenThreshold() {
        return config.getInt("settings.ping_colors.green", 100);
    }

    public int getYellowThreshold() {
        return config.getInt("settings.ping_colors.yellow", 200);
    }

    public String getPreviousButton() {
        return config.getString("pagination.previous_button", "&c[← 上一页]");
    }

    public String getNextButton() {
        return config.getString("pagination.next_button", "&a[下一页 →]");
    }

    public String getPreviousHoverText() {
        return config.getString("pagination.button_hover_text.previous", "&7点击查看上一页");
    }

    public String getNextHoverText() {
        return config.getString("pagination.button_hover_text.next", "&7点击查看下一页");
    }
}
