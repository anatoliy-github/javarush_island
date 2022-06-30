package ru.javarush.animalisland.plants;

import ru.javarush.animalisland.IslandSettings;
import ru.javarush.animalisland.Item;

import java.util.Map;

public class Plant extends Item {

    IslandSettings islandSettings = IslandSettings.getInstance();
    private Map<String, Object> allSettings = islandSettings.getSettings();
    private Map<String, Object> plantSetting = (Map<String, Object>) allSettings.get("Plant");

    public Plant() {
        this.emoji = (String) plantSetting.get("emoji");
        this.weight = (double) plantSetting.get("weight");
        this.maxInLocation = (int) plantSetting.get("maxInLocation");
    }

}
