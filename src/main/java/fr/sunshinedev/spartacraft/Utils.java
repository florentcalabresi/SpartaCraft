package fr.sunshinedev.spartacraft;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Utils {

    public static List<Class<?>> getClassesInPackage(String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        String path = packageName.replace('.', '/');

        try {
            // Trouver le chemin du package
            URL packageURL = Thread.currentThread().getContextClassLoader().getResource(path);
            if (packageURL == null) {
                System.out.println("Package " + packageName + " not found");
                return classes;
            }

            File directory = new File(packageURL.getFile());

            // Parcourir tous les fichiers dans le répertoire du package
            for (File file : directory.listFiles()) {
                if (file.getName().endsWith(".class")) {
                    // Obtenir le nom de la classe sans l'extension .class
                    String className = packageName + '.' + file.getName().replace(".class", "");
                    try {
                        // Charger la classe
                        Class<?> clazz = Class.forName(className);
                        classes.add(clazz);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public static <T> List<T> getRandomObjects(List<T> list, int numberOfItems) {
        if (list.size() < numberOfItems) {
            throw new IllegalArgumentException("La liste ne contient pas assez d'éléments.");
        }
        Collections.shuffle(list);
        return new ArrayList<>(list.subList(0, numberOfItems));
    }

}
