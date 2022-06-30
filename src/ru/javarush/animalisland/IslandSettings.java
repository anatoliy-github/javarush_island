package ru.javarush.animalisland;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class IslandSettings {
    private static IslandSettings INSTANCE;
    private HashMap<String, Object> settings;

    private IslandSettings() {
        settings = new HashMap<>();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("src/ru/javarush/animalisland/basic-animals-settings.yaml");
        } catch (FileNotFoundException e) {
            System.out.println("Ошибка чтения файла настроек basic-animals-settings.yaml" + e);
        }
        Yaml yaml = new Yaml();
        settings = yaml.load(inputStream);
    }
    public static IslandSettings getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new IslandSettings();
        }
        return INSTANCE;
    }
    public Map<String, Object> getSettings() {
        return settings;
    }

}
