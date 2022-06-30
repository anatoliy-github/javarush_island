package ru.javarush.animalisland;

import ru.javarush.animalisland.animals.Animal;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class Island {
    Location[][] locations;

    public Island() {
        IslandSettings settings = IslandSettings.getInstance();
        Map<String, Object> settingsMap = settings.getSettings();
        Map<String, Object> islandSizeMap = (Map<String, Object>) settingsMap.get("Island");
        int width = (int) islandSizeMap.get("width");
        int height = (int) islandSizeMap.get("height");
        locations = new Location[height][width];
        for (int i = 0; i < locations.length; i++) {
            for (int j = 0; j < locations[i].length; j++) {
                locations[i][j] = new Location();
                //При создании каждой локации сразу заполнятся растениями и животными
                locations[i][j].growPlant();
                locations[i][j].fillAnimals();
            }
        }
    }

    //запуск процесса питания всем локациям
    public void eatingAnimals() {
        for (int i = 0; i < locations.length; i++) {
            for (int j = 0; j < locations[i].length; j++) {
                locations[i][j].eatingAnimalsInLocation();
            }
        }
    }

    //запуск процесса роста травы всем локациям
    public void growingPlant() {
        for (int i = 0; i < locations.length; i++) {
            for (int j = 0; j < locations[i].length; j++) {
                locations[i][j].growPlant();
            }
        }
    }

    //запуск процесса размножения по всем локациям
    public void reproductionAnimals() {
        for (int i = 0; i < locations.length; i++) {
            for (int j = 0; j < locations[i].length; j++) {
                locations[i][j].reproductionAnimalsInLocation();
            }
        }
    }

    //Движение животных
    public void moveAnimals() {
        for (int i = 0; i < locations.length; i++) {
            for (int j = 0; j < locations[i].length; j++) {
                String text = "\nlocation[" + i + "][" + j + "]:\n";
                //loggingToFile(text);
                //System.out.println("\u001B[31mlocation[" + i + "][" + j + "]\u001B[0m");
                Map<String, List<? extends Item>> mapFromThisLocation = locations[i][j].getItems();
                for(Map.Entry<String, List<? extends Item>> entryBigMap : mapFromThisLocation.entrySet()) {
                    if(entryBigMap.getKey().equals("Plant") || entryBigMap.getKey().equals("Caterpillar")) continue;
                    List<Animal> list = (List<Animal>) entryBigMap.getValue();
                    for(int n = 0; n < list.size(); n++) {
                        Animal animal = list.get(n);
                        if(animal.isFlagChangePosition()) continue;
                        Class clazz = animal.getClass();
                        int speed = animal.getSpeed();
                        int newX = i;
                        int newY = j;
                        //String text2 = animal.getEmoji() + " меняет локацию. Допустимое расстояние " + speed + "\n";
                        //loggingToFile(text2);
                        //System.out.println(animal.getEmoji() + " меняет локацию. Допустимое расстояние " + speed);
                        while(speed > 0) {
                            //0 - верх, 1 - право, 2 - низ, 3 - лево
                            Random random = new Random();
                            int step = random.nextInt(4);
                            if(step == 0 && j > 0) {
                                if(newY != 0) {
                                    newY--;
                                    speed--;
                                } else continue;
                            } else if(step == 1 && i < locations.length - 2) {
                                if(newX != locations.length - 1) {
                                    newX++;
                                    speed--;
                                } else continue;
                            } else if(step == 2 && j < locations[i].length - 2) {
                                if(newY != locations[i].length - 1) {
                                    newY++;
                                    speed--;
                                } else continue;
                            } else if(step == 3 && i > 0) {
                                if(newX != 0) {
                                    newX--;
                                    speed--;
                                } else continue;
                            }
                        }
                        animal.setFlagChangePosition(true);
                        locations[newX][newY].insertItem(clazz, animal);
                        //String text3 = animal.getEmoji() + " переехал в location[" + newX + "][" + newY + "]\n";
                        //loggingToFile(text3);
                        //System.out.println(animal.getEmoji() + " переехал в location[" + newX + "][" + newY + "]");
                        list.remove(animal);
                    }
                }
            }
        }
        //После того, как все совершили перемещение, обнулить у всех boolean flag
        for (int i = 0; i < locations.length; i++) {
            for (int j = 0; j < locations[i].length; j++) {
                Map<String, List<? extends Item>> mapFromThisLocation = locations[i][j].getItems();
                for(Map.Entry<String, List<? extends Item>> entryBigMap : mapFromThisLocation.entrySet()) {
                    if(entryBigMap.getKey().equals("Plant") || entryBigMap.getKey().equals("Caterpillar")) continue;
                    List<Animal> list = (List<Animal>) entryBigMap.getValue();
                    for(int n = 0; n < list.size(); n++) {
                        Animal animal = list.get(n);
                        animal.setFlagChangePosition(false);
                    }
                }
            }
        }
    }

    public int getCountPredators() {
        int count = 0;
        for (int i = 0; i < locations.length; i++) {
            for (int j = 0; j < locations[i].length; j++) {
                count += locations[i][j].getCountPredatorsInLocation();
            }
        }
        return count;
    }

    //Визуальное отображение острова в виде эмоджи-элементов в локациях
    public void display() {
        for (int i = 0; i < locations.length; i++) {
            for (int j = 0; j < locations[i].length; j++) {
                System.out.print("[");
                Map<String, List<? extends Item>> mapItems = locations[i][j].getItems();
                for(Map.Entry<String, List<? extends Item>> entry : mapItems.entrySet()) {
                    for(var el : entry.getValue()) {
                        System.out.print(el.getEmoji() + " ");
                    }
                }
                System.out.print("]");
            }
            System.out.println();
        }
    }
}


