package com.phoenixstudio.pcoresync;

import com.phoenixstudio.pcoresync.command.SyncCommand;
import com.phoenixstudio.pcoresync.config.ConfigLoader;
import com.phoenixstudio.pcoresync.database.Database;
import com.phoenixstudio.pcoresync.integration.permission.LuckPermsIntegration;
import com.phoenixstudio.pcoresync.integration.permission.PermissionIntegration;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
@Getter
public final class PCoreSync extends JavaPlugin {

    private Database database;
    private PermissionIntegration permissionIntegration;
    @Getter private static PCoreSync instance;

    @Override
    public void onEnable() {
        instance = this;
        ConfigLoader.create(this);
        database = Database.initialize(this);
        if (database == null){
            this.getLogger().severe("Failed to initialize database. Please check your configuration.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (!initializePermissionHooks()){
            this.getLogger().log(java.util.logging.Level.WARNING, "Failed to initialize permission integration. Please ensure LuckPerms is installed and enabled.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("hesapeşle").setExecutor(new SyncCommand());
    }

    private boolean initializePermissionHooks(){
        if (getServer().getPluginManager().isPluginEnabled("LuckPerms")){
            permissionIntegration = new LuckPermsIntegration(this);
        }
        return permissionIntegration != null;
    }
}
