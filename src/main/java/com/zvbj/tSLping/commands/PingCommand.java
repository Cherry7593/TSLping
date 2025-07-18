package com.zvbj.tSLping.commands;

import com.zvbj.tSLping.TSLping;
import com.zvbj.tSLping.config.ConfigManager;
import com.zvbj.tSLping.handlers.PingHandler;
import com.zvbj.tSLping.pagination.PingPaginator;
import com.zvbj.tSLping.utils.TextUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PingCommand implements CommandExecutor, TabCompleter {
    private final TSLping plugin;
    private final ConfigManager config;
    private final PingPaginator paginator;

    public PingCommand(TSLping plugin, ConfigManager config) {
        this.plugin = plugin;
        this.config = config;
        this.paginator = new PingPaginator(config);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // 检查基本权限
        if (!sender.hasPermission("tslping.use")) {
            sender.sendMessage(TextUtils.colorize(config.getMessage("no_permission")));
            return true;
        }

        if (args.length == 0) {
            sendUsage(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "all":
                handleAllCommand(sender, args);
                break;
            case "reload":
                handleReloadCommand(sender);
                break;
            default:
                handlePlayerCommand(sender, args[0]);
                break;
        }

        return true;
    }

    /**
     * 处理 /tping all 命令
     */
    private void handleAllCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("tslping.all")) {
            sender.sendMessage(TextUtils.colorize(config.getMessage("no_permission")));
            return;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(TextUtils.colorize(config.getMessage("not_player")));
            return;
        }

        Player player = (Player) sender;
        int page = paginator.parsePage(args, 1);
        paginator.showPingList(player, page);
    }

    /**
     * 处理 /tping reload 命令
     */
    private void handleReloadCommand(CommandSender sender) {
        if (!sender.hasPermission("tslping.reload")) {
            sender.sendMessage(TextUtils.colorize(config.getMessage("no_permission")));
            return;
        }

        config.reloadConfig();
        sender.sendMessage(TextUtils.colorize(config.getMessage("config_reloaded")));
    }

    /**
     * 处理 /tping <player> 命令
     */
    private void handlePlayerCommand(CommandSender sender, String playerName) {
        Player targetPlayer = PingHandler.findPlayer(playerName);

        if (targetPlayer == null) {
            sender.sendMessage(TextUtils.colorize(config.getMessage("player_not_found", "player", playerName)));
            return;
        }

        if (!targetPlayer.isOnline()) {
            sender.sendMessage(TextUtils.colorize(config.getMessage("player_offline", "player", playerName)));
            return;
        }

        int ping = PingHandler.getPlayerPing(targetPlayer);
        String message = config.getPlayerPingMessage("ping_format", targetPlayer.getName(), String.valueOf(ping));
        sender.sendMessage(TextUtils.colorize(message));
    }

    /**
     * 发送命令使用说明
     */
    private void sendUsage(CommandSender sender) {
        sender.sendMessage(TextUtils.colorize(config.getMessageWithoutPrefix("help_header")));
        sender.sendMessage(TextUtils.colorize(config.getMessageWithoutPrefix("help_ping_player")));

        if (sender.hasPermission("tslping.all")) {
            sender.sendMessage(TextUtils.colorize(config.getMessageWithoutPrefix("help_ping_all")));
        }

        if (sender.hasPermission("tslping.reload")) {
            sender.sendMessage(TextUtils.colorize(config.getMessageWithoutPrefix("help_reload")));
        }

        sender.sendMessage(TextUtils.colorize(config.getMessageWithoutPrefix("help_footer")));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            List<String> subCommands = new ArrayList<>();

            // 添加在线玩家名称
            subCommands.addAll(plugin.getServer().getOnlinePlayers().stream()
                    .map(Player::getName)
                    .collect(Collectors.toList()));

            // 添加子命令
            if (sender.hasPermission("tslping.all")) {
                subCommands.add("all");
            }
            if (sender.hasPermission("tslping.reload")) {
                subCommands.add("reload");
            }

            return subCommands.stream()
                    .filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("all")) {
            // 为 all 命令提供页码补全
            return Arrays.asList("1", "2", "3");
        }

        return completions;
    }
}
