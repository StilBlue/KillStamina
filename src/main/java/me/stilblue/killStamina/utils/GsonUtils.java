package me.stilblue.killStamina.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GsonUtils {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    public static void write(Plugin plugin, String fileName, Object object) {
        Path path = Paths.get(plugin.getDataFolder().getAbsolutePath() + "/player_data/" + fileName + ".json");
        try {
            if(!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            GSON.toJson(object, bufferedWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> T read(Plugin plugin, String fileName, Type type) {
        Path path = Paths.get(plugin.getDataFolder().getAbsolutePath() + "/player_data/" + fileName + ".json");
        if (!Files.exists(path)) {
            return null;
        }

        try {
            BufferedReader bufferedReader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
            JsonReader jsonReader = new JsonReader(bufferedReader);
            return GSON.fromJson(jsonReader, type);
        } catch (IOException e) {
            return null;
        }
    }

    public static void delete(Plugin plugin, String fileName) {
        Path path = Paths.get(plugin.getDataFolder().getAbsolutePath() + "/player_data/" + fileName + ".json");
        try {
            if(Files.exists(path))
                Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
