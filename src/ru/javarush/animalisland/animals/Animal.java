package ru.javarush.animalisland.animals;

import ru.javarush.animalisland.Item;
import java.util.Map;

public abstract class Animal extends Item {
    protected int speed;
    protected double foodEnough;
    protected Map<String,Integer> probabilityEating;
    protected boolean flagChangePosition = false;
    protected int satiety = 3; //Каждое животное способно продержаться без еды 3 дня

    public boolean isFlagChangePosition() {
        return flagChangePosition;
    }

    public void setFlagChangePosition(boolean flagChangePosition) {
        this.flagChangePosition = flagChangePosition;
    }

    public int getSpeed() {
        return speed;
    }

    public double getFoodEnough() {
        return foodEnough;
    }

    public Map<String, Integer> getProbabilityEating() {
        return probabilityEating;
    }

    public int getSatiety() {
        return satiety;
    }

    public void setSatiety(int satiety) {
        this.satiety = satiety;
    }

}
