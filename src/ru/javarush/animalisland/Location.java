package ru.javarush.animalisland;

import ru.javarush.animalisland.animals.Animal;
import ru.javarush.animalisland.animals.herbivores.*;
import ru.javarush.animalisland.animals.predators.*;
import ru.javarush.animalisland.plants.Plant;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class Location {
    private Map<String, List<? extends Item>> mapItems = new ConcurrentHashMap<>();
    IslandSettings settings = IslandSettings.getInstance();
    Map<String, Object> settingsMap = settings.getSettings();
    Map<String, Object> setingsAnimals = (Map<String, Object>) settingsMap.get("Animal");
    Map<String, Object> settingsPlant = (Map<String, Object>) settingsMap.get("Plant");

    public Location() {
        //Пока не нашел умного решения как инициализировать списки
        //LinkedList, потому что удалять буду нулевой элемент
        mapItems.put("Wolf", new LinkedList<Wolf>());
        mapItems.put("Python", new LinkedList<Python>());
        mapItems.put("Fox", new LinkedList<Fox>());
        mapItems.put("Eagle", new LinkedList<Eagle>());
        mapItems.put("Bear", new LinkedList<Bear>());
        mapItems.put("Sheep", new LinkedList<Sheep>());
        mapItems.put("Rabbit", new LinkedList<Rabbit>());
        mapItems.put("Mouse", new LinkedList<Mouse>());
        mapItems.put("Horse", new LinkedList<Horse>());
        mapItems.put("Hog", new LinkedList<Hog>());
        mapItems.put("Goat", new LinkedList<Goat>());
        mapItems.put("Duck", new LinkedList<Duck>());
        mapItems.put("Deer", new LinkedList<Deer>());
        mapItems.put("Buffalo", new LinkedList<Buffalo>());
        mapItems.put("Caterpillar", new LinkedList<Caterpillar>());
        mapItems.put("Plant", new LinkedList<Plant>());
    }

    public Map getItems() {
        return mapItems;
    }

    //Заполнение локации растениями
    public void growPlant() {
        int maxInLocation = (int) settingsPlant.get("maxInLocation");
        List<Plant> plantList = (List<Plant>) mapItems.get("Plant");
        int currentCountPlants = plantList.size();
        int range = maxInLocation - currentCountPlants;
        if(range > 1) {
            //Random r = new Random();
            //int count = r.nextInt(1, range + 1);
            int count = ThreadLocalRandom.current().nextInt(1,range + 1);
            for (int i = 0; i < count; i++) {
                plantList.add(new Plant());
            }
        }
    }

    //Заполнение локации животными
    public void fillAnimals() {
        for(Map.Entry<String, Object> entryType : setingsAnimals.entrySet()) {
            String typeAnimal = entryType.getKey();//predators or herbivores
            Map<String, Object> settingTypeAnimals = (Map<String, Object>) entryType.getValue();
            for(Map.Entry<String, Object> entryAnimal : settingTypeAnimals.entrySet()) {
                String classAnimal = entryAnimal.getKey();//Wolf, Rabbit ...
                String className = "ru.javarush.animalisland.animals." + typeAnimal + "." + classAnimal;
                Map<String, Object> setingsThisAnimal = (Map<String, Object>) entryAnimal.getValue();
                int maxInLocation = (int) setingsThisAnimal.get("maxInLocation");
                //Random r = new Random();
                //int count = r.nextInt(1, maxInLocation + 1);
                int count = ThreadLocalRandom.current().nextInt(1, maxInLocation + 1);
                try {
                    Class<?> clazz = Class.forName(className);
                    Constructor<?> constructor = clazz.getConstructor();
                    for (int i = 0; i < count; i++) {
                        insertItem(clazz, (Animal) constructor.newInstance());
                    }
                } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
                         IllegalAccessException | InvocationTargetException e) {
                    System.out.println("Ошибка создания класса " + className + " | " + e);
                }
            }
        }
    }

    //Получить список животных по классу
    private <E> List<E> getListByClass(Class<E> clazz) {
        return (List<E>) mapItems.get(clazz.getSimpleName());
    }
    //вставка элемента в типизированный список
    <T> void insertItem(Class<T> clazz, Animal animal) {
        List<T> list = getListByClass(clazz);
        list.add((T) animal);
    }

    //Питание всех животных в локации
    public void eatingAnimalsInLocation() {
        for(Map.Entry<String, List<? extends Item>> entry : mapItems.entrySet()) {
            if(entry.getKey().equals("Plant")) continue;
            List<Animal> currentAnimalLIst = (List<Animal>) entry.getValue();
            Iterator iterator = currentAnimalLIst.iterator();
            while(iterator.hasNext()) {
                Animal animal = (Animal) iterator.next();
                Map<String, Integer> probabilityEating = animal.getProbabilityEating();
                //Возьмем случайный вид животного из возможных быть съеденными
                List<String> probabilityVictim = new ArrayList<>();
                probabilityVictim.addAll(probabilityEating.keySet());
                //Random rand = new Random();
                for (int i = 0; i < probabilityVictim.size(); i++) {

                    String randomVictim = probabilityVictim.get(ThreadLocalRandom.current().nextInt(probabilityVictim.size()));
                    if(mapItems.get(randomVictim).size() > 0) {
                        int probability = probabilityEating.get(randomVictim);
                        //Random r = new Random();
                        //int randomInt = r.nextInt(101);
                        int randomInt = ThreadLocalRandom.current().nextInt(101);
                        if(randomInt < probability) {
                            mapItems.get(randomVictim).remove(0);
                            //System.out.println(animal.getEmoji() + " съел " + randomVictim);
                        } else {
                            //System.out.println(animal.getEmoji() + " не съел " + randomVictim);
                        }
                        break;
                    } else {
                        int satiety = animal.getSatiety();
                        satiety--;
                        if(satiety == 0) {
                            iterator.remove();
                            //System.out.println(animal.getEmoji() + " умер от голода");
                        } else {
                            animal.setSatiety(satiety);
                        }
                        break;
                    }
                }
            }
        }
    }

    //Размножение животных
    public void reproductionAnimalsInLocation() {
        for(Map.Entry<String, List<? extends Item>> entry : mapItems.entrySet()) {
            if(entry.getKey().equals("Plant")) continue;
            String className = entry.getKey();
            Map<String, Object> thisAnimalSettings = (Map<String, Object>) setingsAnimals.get(className);
            int maxChild = (int) thisAnimalSettings.get("maxChild");
            List<Animal> currentAnimalLIst = (List<Animal>) entry.getValue();
            int countReadyReproduce = 0;

            if(currentAnimalLIst.size() > 1) {
                for (Animal animal : currentAnimalLIst) {
                    //готово ли данное животное к размножению:
                    if(ThreadLocalRandom.current().nextBoolean()) {
                        countReadyReproduce++;
                    }
                }
                //если готовых к размножению больше 2 и делим их на пары
                if(countReadyReproduce > 1) {
                    int couple = countReadyReproduce / 2;
                    System.out.println(couple + " пары " + className + " готовы дать потомство");
                    Random rand = new Random();
                    for (int i = 0; i < couple; i++) {
                        int newChildCount = rand.nextInt(maxChild + 1);
                        for (int j = 0; j < newChildCount; j++) {

                        }
                    }
                }
            }
        }
    }

    public int getCountPredatorsInLocation() {
        AtomicInteger count = new AtomicInteger(0);

        Map<String, Object> settingsPredators = (Map<String, Object>) setingsAnimals.get("predators");
        for(Map.Entry<String, Object> entry : settingsPredators.entrySet()) {
            String className = entry.getKey();
            List<Animal> list = (List<Animal>) mapItems.get(className);
            count.updateAndGet(n -> n + list.size());
        }
        return count.get();
    }
}