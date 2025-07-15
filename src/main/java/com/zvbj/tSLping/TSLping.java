package com.zvbj.tSLping;

import com.zvbj.tSLping.commands.PingCommand;
import com.zvbj.tSLping.config.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class TSLping extends JavaPlugin {

    private ConfigManager configManager;
    private PingCommand pingCommand;

    @Override
    public void onEnable() {
        // 初始化配置管理器
        this.configManager = new ConfigManager(this);

        // 初始化命令处理器
        this.pingCommand = new PingCommand(this, configManager);

        // 注册命令和Tab补全
        getCommand("tping").setExecutor(pingCommand);
        getCommand("tping").setTabCompleter(pingCommand);

        // 插件启动成功消息
        getLogger().info("TSLping 插件已成功启用！");
        getLogger().info("支持命令: /tping <player>, /tping all, /tping reload");
    }

    @Override
    public void onDisable() {
        getLogger().info("TSLping 插件已关闭！");
    }

    /**
     * 获取配置管理器
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }
}
