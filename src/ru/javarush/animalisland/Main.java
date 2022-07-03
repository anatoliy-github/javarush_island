package ru.javarush.animalisland;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Island island = new Island();
        Location[][] locations = island.locations;

        ExecutorService executorService = Executors.newFixedThreadPool(8);
        //Phaser phaser = new Phaser(2);

        int day = 1;
        while(island.getCountPredators() > 0) {
            System.out.println("DAY " + day);
            for (int i = 0; i < locations.length; i++) {
                for (int j = 0; j < locations[i].length; j++) {
                    Location currentLocation = locations[i][j];
                    executorService.submit(() -> currentLocation.eatingAnimalsInLocation());
                }
            }

            for (int i = 0; i < locations.length; i++) {
                for (int j = 0; j < locations[i].length; j++) {
                    Location currentLocation = locations[i][j];
                    executorService.submit(() -> currentLocation.growPlant());
                }
            }

            island.moveAnimals();
            day++;
        }
        System.out.println("Игра закончилась на " + day + " день. Все хищники вымерли");

        executorService.shutdown();
        try {
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //Однопоточный цикл
//        int day = 1;
//        while(island.getCountPredators() > 0) {
//            System.out.println("DAY " + day);
//            System.out.println("Хищников: " + island.getCountPredators());
//            island.eatingAnimals();
//            island.moveAnimals();
//            island.growingPlant();
//            day++;
//        }
//        System.out.println("Игра закончилась на " + day + " день. Все хищники вымерли");


    }
}
