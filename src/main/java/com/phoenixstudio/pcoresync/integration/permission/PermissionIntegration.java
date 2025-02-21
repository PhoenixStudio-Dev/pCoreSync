package com.phoenixstudio.pcoresync.integration.permission;

import org.bukkit.plugin.Plugin;

import java.util.UUID;

public abstract class PermissionIntegration {

    public PermissionIntegration(Plugin plugin) {}

    public abstract boolean hasPermission(UUID uuid, String permission);
}
