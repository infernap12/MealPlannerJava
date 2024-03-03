package mealplanner;

import java.awt.*;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
        String category = scanner.nextLine();
        System.out.println("Input the meal's name:");
        String name = scanner.nextLine();
        System.out.println("Input the ingredients:");
        String ingredients = scanner.nextLine();

        System.out.println("Category: " + category);
        System.out.println("Name: " + name);
        System.out.println("Ingredients:");
        Arrays.stream(ingredients.split(",")).iterator().forEachRemaining(System.out::println);
        System.out.println("The meal has been added!");
    }
}