package mealplanner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
    static MealDAOImpl mealDAO;
    private static PlanDAOImpl planDAO;

    public static void main(String[] args) {
        try {
            Database.initDB();
            mealDAO = new MealDAOImpl();
            planDAO = new PlanDAOImpl();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Scanner scanner = new Scanner(System.in);
        while (true) {//menu
            System.out.println("What would you like to do (add, show, plan, exit)?");
            String choice = scanner.nextLine();
            switch (choice) {
                case "add" -> add();
                case "show" -> show();
                case "plan" -> plan();
                case "exit" -> {
                    Database.close();
                    System.out.println("Bye!");
                    System.exit(0);
                }
            }
        }
    }

    private static void plan() {
        Scanner scanner = new Scanner(System.in);
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        String[] categories = {"Breakfast", "Lunch", "Dinner"};
        for (String day : days) {
            System.out.println(day);
            for (String category : categories) {
                ArrayList<Meal> list = mealDAO.fetchByCategory(category.toLowerCase());
                Collections.sort(list);
                list.forEach(e -> System.out.println(e.name()));
                System.out.printf("Choose the %s for %s from the list above:%n", category.toLowerCase(), day);
                Meal meal;
                while (true) {
                    String choice = scanner.nextLine();
                    meal = mealDAO.findByName(choice);
                    if (meal != null) {
                        break;
                    }
                    System.out.println("This meal doesn’t exist. Choose a meal from the list above.");
                }
                planDAO.insert(new Plan(day, category, meal.id(), 999));
            }
            System.out.printf("Yeah! We planned the meals for %s.%n%n", day);
        }
        for (String day : days) {
            System.out.println(day);
            for (String category : categories) {
                Plan plan = planDAO.findByDayCat(day, category);
                System.out.println("%s: %s".formatted(category, mealDAO.findById(plan.meal_id()).name()));
            }
            System.out.println();
        }
    }

    private static void show() {
        ArrayList<Meal> list;
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Which category do you want to print (breakfast, lunch, dinner)?");
            String input = scanner.nextLine();
            if (!input.matches("breakfast|lunch|dinner")) {
                System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
                continue;
            }
            list = mealDAO.fetchByCategory(input);
            if (list.isEmpty()) {
                System.out.println("No meals found.");
                return;
            } else {
                System.out.println("Category: " + input);
                break;
            }
        }
        list.forEach(Main::printMeal);
        System.out.println();
    }


    public static void add() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
        String category;
        while (true) {
            category = scanner.nextLine();
            if (category.matches("breakfast|lunch|dinner")) {
                break;
            } else {
                System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            }
        }
        System.out.println("Input the meal's name:");
        String name;
        while (true) {
            name = scanner.nextLine();
            if (name.matches("[a-z ]+")) {
                break;
            } else {
                System.out.println("Wrong format. Use letters only!");
            }
        }
        System.out.println("Input the ingredients:");
        String[] arr;
        while (true) {
            String ingredients = scanner.nextLine();
            arr = ingredients.split(",");
            boolean valid = true;
            for (int i = 0; i < arr.length; i++) {
                arr[i] = arr[i].trim();
                if (!arr[i].matches("[a-z ]+")) {
                    valid = false;
                }
            }
            if (valid) {
                break;
            } else {
                System.out.println("Wrong format. Use letters only!");
            }

        }
        mealDAO.insert(new Meal(999, category, name, new ArrayList<>(List.of(arr))));
        System.out.println("The meal has been added!");
    }

    public static void printMeal(Meal meal) {
        System.out.println();
        System.out.println(meal.toString());
    }
}

