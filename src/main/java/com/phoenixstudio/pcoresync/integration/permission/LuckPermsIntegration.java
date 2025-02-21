package com.phoenixstudio.pcoresync.integration.permission;

import com.phoenixstudio.pcoresync.PCoreSync;
import com.phoenixstudio.pcoresync.config.ConfigLoader;
import com.phoenixstudio.pcoresync.util.RequestUtil;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.event.node.NodeRemoveEvent;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class LuckPermsIntegration extends PermissionIntegration {

    private LuckPerms api;

    public LuckPermsIntegration(Plugin plugin) {
        super(plugin);
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            api = provider.getProvider();
            plugin.getLogger().log(Level.INFO, "LuckPerms Integration is enabled!");
        }
        EventBus eventBus = api.getEventBus();
        eventBus.subscribe(plugin, NodeAddEvent.class, this::onPermissionAdded);
        eventBus.subscribe(plugin, NodeRemoveEvent.class, this::onPermissionDeleted);
    }

    @Override
    public boolean hasPermission(UUID uuid, String permission) {
        User user = api.getUserManager().getUser(uuid);
        return user != null && user.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
    }

    public void onPermissionAdded(NodeAddEvent event){
        if (!event.isUser()) return;
        User user = (User) event.getTarget();
        Node node = event.getNode();
        String permission = event.getNode().getKey();
        // filter and add permissions of player
        for (Map.Entry<String, List<Map<String,String>>> values : ConfigLoader.getConfig().getRoles().getRoles().entrySet()){
            String requiredPermission = values.getValue().get(1).get("requiredPermission");
            if (permission.equals(requiredPermission)){
                RequestUtil.sendUpdateRequest(user.getUniqueId(), RequestUtil.UpdateType.ADD, List.of(node.getKey()));
            }
        }
    }
    public void onPermissionDeleted(NodeRemoveEvent event){
        if (!event.isUser()) return;
        User user = (User) event.getTarget();
        Node node = event.getNode();
        String permission = event.getNode().getKey();
        // filter and remove permissions of player
        for (Map.Entry<String, List<Map<String,String>>> values : ConfigLoader.getConfig().getRoles().getRoles().entrySet()){
            String requiredPermission = values.getValue().get(1).get("requiredPermission");
            if (permission.equals(requiredPermission)){
                RequestUtil.sendUpdateRequest(user.getUniqueId(), RequestUtil.UpdateType.REMOVE, List.of(node.getKey()));
            }
        }
    }
}
