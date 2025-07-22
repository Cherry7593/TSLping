package com.zvbj.tSLping.config;

import com.zvbj.tSLping.TSLping;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class ConfigManager {
    private final TSLping plugin;
    private FileConfiguration config;
    private FileConfiguration messageConfig;
    private File messageFile;

    public ConfigManager(TSLping plugin) {
        this.plugin = plugin;
        loadConfig();
        loadMessageConfig();
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    private void loadMessageConfig() {
        messageFile = new File(plugin.getDataFolder(), "message.yml");

        // 如果message.yml不存在，从resources中复制
        if (!messageFile.exists()) {
            try {
                InputStream inputStream = plugin.getResource("message.yml");
                if (inputStream != null) {
                    Files.copy(inputStream, messageFile.toPath());
                    inputStream.close();
                }
            } catch (IOException e) {
                plugin.getLogger().severe("无法创建message.yml文件: " + e.getMessage());
            }
        }

        messageConfig = YamlConfiguration.loadConfiguration(messageFile);
    }

    public void reloadConfig() {
        loadConfig();
        loadMessageConfig();
    }

    /**
     * 获取前缀
     */
    public String getPrefix() {
        return config.getString("prefix", "&6[TSL喵]&r ");
    }

    /**
     * 获取消息（带前缀）
     */
    public String getMessage(String key) {
        String message = messageConfig.getString(key, "&c配置项 " + key + " 未找到");
        return getPrefix() + message;
    }

    /**
     * 获取消息（不带前缀）
     */
    public String getMessageWithoutPrefix(String key) {
        return messageConfig.getString(key, "&c配置项 " + key + " 未找到");
    }

    /**
     * 获取消息并替换单个占位符
     */
    public String getMessage(String key, String placeholder, String value) {
        return getMessage(key).replace("{" + placeholder + "}", value);
    }

    /**
     * 获取消息并替换玩家和延迟占位符（专用方法）
     */
    public String getPlayerPingMessage(String key, String player, String ping) {
        return getMessage(key)
                .replace("{player}", player)
                .replace("{ping}", ping);
    }

    /**
     * 获取消息并替换多个占位符（通用方法）
     */
    public String getMessageWithReplacements(String key, String... replacements) {
        String message = getMessage(key);
        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 < replacements.length) {
                message = message.replace("{" + replacements[i] + "}", replacements[i + 1]);
            }
        }
        return message;
    }

    /**
     * 获取分页标题
     */
    public String getPageHeader(int page, int totalPages) {
        return getMessage("page_header", "page", String.valueOf(page))
                .replace("{total_pages}", String.valueOf(totalPages));
    }

    /**
     * 获取每页条目数
     */
    public int getEntriesPerPage() {
        return config.getInt("settings.entries_per_page", 10);
    }

    /**
     * 获取延迟颜色阈值
     */
    public int getGreenThreshold() {
        return config.getInt("settings.ping_colors.green", 100);
    }

    public int getYellowThreshold() {
        return config.getInt("settings.ping_colors.yellow", 200);
    }

    /**
     * 获取分页按钮文本
     */
    public String getPreviousButton() {
        return getMessageWithoutPrefix("pagination.previous_button");
    }

    public String getNextButton() {
        return getMessageWithoutPrefix("pagination.next_button");
    }

    /**
     * 获取悬停文本
     */
    public String getPreviousHover() {
        return getMessageWithoutPrefix("pagination.page_navigation_hover.previous");
    }

    public String getNextHover() {
        return getMessageWithoutPrefix("pagination.page_navigation_hover.next");
    }

    /**
     * 获取服务器平均延迟消息
     */
    public String getAveragePingMessage(double averagePing) {
        String formattedPing = String.format("%.1f", averagePing);
        return getMessage("server_average_ping", "average_ping", formattedPing);
    }
}
