package mealplanner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static MealDAOImpl mealDAO;
    public static void main(String[] args) {
        try {
            Database.initDB();
            mealDAO = new MealDAOImpl();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Scanner scanner = new Scanner(System.in);
        while (true) {//menu
            System.out.println("What would you like to do (add, show, exit)?");
            String choice = scanner.nextLine();
            switch (choice) {
                case "add" -> add();
                case "show" -> show();
                case "exit" -> {
                    Database.close();
                    System.out.println("Bye!");
                    System.exit(0);
                }
            }
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
        try {
            Database.addMeal(new Meal(999, category, name, new ArrayList<>(List.of(arr))));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("The meal has been added!");
    }

    public static void printMeal(Meal meal) {
        System.out.println();
        System.out.println(meal.toString());
    }
}

