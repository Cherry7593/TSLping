package com.zvbj.tSLping.handlers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class PingHandler {

    /**
     * 获取指定玩家的延迟
     */
    public static int getPlayerPing(Player player) {
        return player.getPing();
    }

    /**
     * 获取所有在线玩家的延迟信息，按延迟从低到高排序
     */
    public static List<PlayerPingInfo> getAllPlayersPing() {
        return Bukkit.getOnlinePlayers().stream()
                .map(player -> new PlayerPingInfo(player.getName(), player.getPing()))
                .sorted(Comparator.comparingInt(PlayerPingInfo::getPing))
                .collect(Collectors.toList());
    }

    /**
     * 根据玩家名称查找在线玩家
     */
    public static Player findPlayer(String playerName) {
        return Bukkit.getPlayer(playerName);
    }

    /**
     * 检查玩家是否在线
     */
    public static boolean isPlayerOnline(String playerName) {
        return Bukkit.getPlayer(playerName) != null;
    }

    /**
     * 玩家Ping信息类
     */
    public static class PlayerPingInfo {
        private final String playerName;
        private final int ping;

        public PlayerPingInfo(String playerName, int ping) {
            this.playerName = playerName;
            this.ping = ping;
        }

        public String getPlayerName() {
            return playerName;
        }

        public int getPing() {
            return ping;
        }
    }
}
