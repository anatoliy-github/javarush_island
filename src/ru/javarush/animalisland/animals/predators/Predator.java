package ru.javarush.animalisland.animals.predators;

import ru.javarush.animalisland.IslandSettings;
import ru.javarush.animalisland.animals.Animal;

import java.util.Map;

public abstract class Predator extends Animal {

    IslandSettings islandSettings = IslandSettings.getInstance();
    private Map<String, Object> allSettings = islandSettings.getSettings();
    private Map<String, Object> animalSetting = (Map<String, Object>) allSettings.get("Animal");
    private Map<String, Object> typeSetting = (Map<String, Object>) animalSetting.get("predators");

    public Predator() {
        Class clazz = this.getClass();
        Map<String, Object> thisSetting = (Map<String, Object>) typeSetting.get(clazz.getSimpleName());
        this.emoji = (String) thisSetting.get("emoji");
        this.weight = (double) thisSetting.get("weight");
        this.maxInLocation = (int) thisSetting.get("maxInLocation");
        this.speed = (int) thisSetting.get("speed");
        this.foodEnough = (double) thisSetting.get("foodEnough");
        this.probabilityEating = (Map<String, Integer>) thisSetting.get("probabilityEating");
    }
}
