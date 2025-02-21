package com.phoenixstudio.pcoresync.database;

import com.mongodb.client.MongoClients;
import com.phoenixstudio.pcoresync.config.ConfigLoader;
import com.phoenixstudio.pcoresync.model.PlayerData;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import org.bson.Document;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class Database {

    private Datastore datastore;

    private Database(Plugin plugin){
        datastore = Morphia.createDatastore(MongoClients.create(ConfigLoader.getConfig().getMongoUri()), ConfigLoader.getConfig().getMongoDb());
        datastore.ensureIndexes();
        plugin.getLogger().info("Connected to MongoDB successfully!");
    }

    public static Database initialize(Plugin plugin){
        try {
            return new Database(plugin);
        }catch (IllegalArgumentException ex){
            return null;
        }
    }

    public boolean hasPlayer(UUID uuid){
        return datastore.find(uuid.toString(), String.class).count() > 0;
    }

    public PlayerData getPlayerData(UUID uuid){
        Document data = datastore.find(uuid.toString(),String.class).toDocument();
        return new PlayerData(data.getString("discordID"), data.getString("discordName"));
    }
}

