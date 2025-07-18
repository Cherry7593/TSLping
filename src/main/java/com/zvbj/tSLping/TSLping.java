package com.zvbj.tSLping;

import com.zvbj.tSLping.commands.PingCommand;
import com.zvbj.tSLping.config.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class TSLping extends JavaPlugin {

    private ConfigManager configManager;

    @Override
    public void onEnable() {
        // 初始化配置管理器
        this.configManager = new ConfigManager(this);

        // 初始化命令处理器
        PingCommand pingCommand = new PingCommand(this, configManager);

        // 注册命令和Tab补全，添加空值检查
        if (getCommand("tping") != null) {
            getCommand("tping").setExecutor(pingCommand);
            getCommand("tping").setTabCompleter(pingCommand);
        } else {
            getLogger().severe("无法注册命令 'tping'，请检查plugin.yml配置");
            return;
        }

        // 插件启动成功消息 - 使用配置的消息
        getLogger().info(configManager.getMessageWithoutPrefix("success.plugin_enabled"));
        getLogger().info("支持命令: /tping <player>, /tping all, /tping reload");
    }

    @Override
    public void onDisable() {
        getLogger().info(configManager != null ?
            configManager.getMessageWithoutPrefix("success.plugin_disabled") :
            "TSLping 插件已关闭！");
    }
}
