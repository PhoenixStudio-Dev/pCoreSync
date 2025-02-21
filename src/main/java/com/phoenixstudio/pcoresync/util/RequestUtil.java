package com.phoenixstudio.pcoresync.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.phoenixstudio.pcoresync.config.ConfigLoader;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RequestUtil {

    @SneakyThrows
    public static String sendVerifyRequest(Player player) {
        String code = CodeUtil.generateUniqueCode(player.getUniqueId());
        if (code == null) return null;
        List<String> roleIds = new ArrayList<>();

        // filter and add permissions of player
        for (Map.Entry<String, Map<String,String>> values : ConfigLoader.getConfig().getRoles().getRoles().entrySet()){
            String permission = values.getValue().get("requiredPermission");
            if (player.hasPermission(permission)){
                roleIds.add(values.getValue().get("roleId"));
            }
        }

        String url = ConfigLoader.getConfig().getDiscordBotServerIp() + "/verify";
        HttpClient client = HttpClient.newHttpClient();

        JsonObject object = new JsonObject();
        object.addProperty("uuid", String.valueOf(player.getUniqueId()));
        JsonArray jsonArray = new JsonArray();
        for (String roleId : roleIds) {
            jsonArray.add(roleId);
        }
        object.add("roleIds", jsonArray);
        String jsonInputString = object.toString();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonInputString))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return code;
    }


    @SneakyThrows
    public static boolean sendUpdateRequest(UUID uuid, UpdateType type, List<String> roleIds){
        String url = ConfigLoader.getConfig().getDiscordBotServerIp() + "/update";
        HttpClient client = HttpClient.newHttpClient();
        JsonObject object = new JsonObject();
        object.addProperty("type", type.name());
        object.addProperty("uuid", String.valueOf(uuid));
        JsonArray jsonArray = new JsonArray();
        for (String roleId : roleIds) {
            jsonArray.add(roleId);
        }
        object.add("roleIds", jsonArray);
        String jsonInputString = object.toString();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonInputString))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200;
    }

    public enum UpdateType {
        ADD,REMOVE
    }

}
