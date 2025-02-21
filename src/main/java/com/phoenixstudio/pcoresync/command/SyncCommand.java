package com.phoenixstudio.pcoresync.command;

import com.phoenixstudio.pcoresync.PCoreSync;
import com.phoenixstudio.pcoresync.config.ConfigLoader;
import com.phoenixstudio.pcoresync.model.PlayerData;
import com.phoenixstudio.pcoresync.util.RequestUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.phoenixstudio.pcoresync.util.CodeUtil;

public class SyncCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player){
            if (PCoreSync.getInstance().getDatabase().hasPlayer(player.getUniqueId())){
                PlayerData data = PCoreSync.getInstance().getDatabase().getPlayerData(player.getUniqueId());
                for (String line : ConfigLoader.getConfig().getMessages().getInfoMessage()){
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',line)
                            .replaceAll("%discord-name%", data.getDiscordName())
                            .replaceAll("%discord-id%", data.getDiscordId()));
                }
                return false;
            }
            String sendRequest = RequestUtil.sendVerifyRequest(player);
            if (sendRequest == null){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigLoader.getConfig().getMessages().getErrorMessage().replaceAll("%code%", CodeUtil.getCodes().get(player.getUniqueId()))));
                return false;
            }
            for (String line : ConfigLoader.getConfig().getMessages().getCodeMessage()){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', line.replaceAll("%code%", sendRequest)));
            }
        }
        return false;
    }
}
