package com.phoenixstudio.pcoresync.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.CustomKey;
import eu.okaeri.configs.annotation.Header;
import eu.okaeri.configs.annotation.Variable;
import lombok.Getter;

import java.util.List;
import java.util.Map;
@Getter
@Header("###############################################################")
@Header(" # Phoenix Core Sync Configuration")
@Header(" # Contact: discord.phoenixstudio.com.tr")
@Header("###############################################################")
public class Config extends OkaeriConfig {

    @Variable("prefix")
    private String prefix = "&bPhoenixStudio &8>>";

    @CustomKey("roles")
    private RoleConfiguration roles = new RoleConfiguration();

    @CustomKey("messages")
    private Messages messages = new Messages();

    @Variable("discordBotServerIp")
    @Comment({"Eğer discord botunuzu sunucudaki makinede çalıştırmıyorsanız", "Lütfen çalıştırdığınız sunucunun paylaşılan ip adresini giriniz.", "Default: localhost"})
    private String discordBotServerIp = "localhost";

    @Variable("mongoUri")
    @Comment({"Lütfen buraya discord botunuza bağlı mongoDB bağlantısını bırakınız!"})
    private String mongoUri = "";
    @Variable("mongoDb")
    @Comment({"Lütfen buraya discord botunuza bağlı mongoDB database ismini bırakınız!"})
    private String mongoDb = "pCoreSync";

    @Getter
    public static class RoleConfiguration extends OkaeriConfig {
        private Map<String, List<Map<String,String>>> roles = Map.of(
                "vip",List.of(Map.of("requiredPermission", "sunucunuz.vip"), Map.of("roleId", "<role-id>")),
                "mvip", List.of(Map.of("requiredPermission", "sunucunuz.mvip"), Map.of("roleId", "<role-id>"))
        );
    }

    @Getter
    public static class Messages extends OkaeriConfig{

        private List<String> codeMessage = List.of(
                "&8",
                "&eHesap eşleştirmesi için gereken kod: &b%code%",
                "&8"
        );

        private List<String> infoMessage = List.of(
                "&8",
                "&eDiscord ID: &a%discord-id%",
                "&eDiscord İsim: &a%discord-name%",
                "&8"
        );
        private String ErrorMessage = "&cİşlem sırasında bir sorun yaşanıldı!";
    }
}
